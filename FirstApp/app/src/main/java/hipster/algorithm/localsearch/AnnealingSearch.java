package hipster.algorithm.localsearch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import hipster.algorithm.Algorithm;
import hipster.model.HeuristicNode;
import hipster.model.Node;
import hipster.model.function.NodeExpander;

public class AnnealingSearch<A, S, N extends HeuristicNode<A, S, Double, N>> extends Algorithm<A, S, N> {

	static final private Double DEFAULT_ALPHA = 0.9;
	static final private Double DEFAULT_MIN_TEMP = 0.00001;
	static final private Double START_TEMP = 1.;

	private N initialNode;
	private Double alpha;
	private Double minTemp;
	private AcceptanceProbability acceptanceProbability;
	private SuccessorFinder<A, S, N> successorFinder;
	// expander to find all the successors of a given node.
	private NodeExpander<A, S, N> nodeExpander;

	public AnnealingSearch(N initialNode, NodeExpander<A, S, N> nodeExpander, Double alpha, Double minTemp,
			AcceptanceProbability acceptanceProbability, SuccessorFinder<A, S, N> successorFinder) {
		if (initialNode == null) {
			throw new IllegalArgumentException("Provide a valid initial node");
		}
		this.initialNode = initialNode;
		if (nodeExpander == null) {
			throw new IllegalArgumentException("Provide a valid node expander");
		}
		this.nodeExpander = nodeExpander;
		if (alpha != null) {
			if ((alpha <= 0.) || (alpha >= 1.0)) {
				throw new IllegalArgumentException("alpha must be between 0. and 1.");
			}
			this.alpha = alpha;
		} else {
			this.alpha = DEFAULT_ALPHA;
		}
		if (minTemp != null) {
			if ((minTemp < 0.) || (minTemp > 1.)) {
				throw new IllegalArgumentException("Minimum temperature must be between 0. and 1.");
			}
			this.minTemp = minTemp;
		} else {
			this.minTemp = DEFAULT_MIN_TEMP;
		}
		if (acceptanceProbability != null) {
			this.acceptanceProbability = acceptanceProbability;
		} else {
			this.acceptanceProbability = new AcceptanceProbability() {
				@Override
				public Double compute(Double oldScore, Double newScore, Double temp) {
					return (newScore < oldScore ? 1 : Math.exp((oldScore - newScore) / temp));
				}
			};
		}
		if (successorFinder != null) {
			this.successorFinder = successorFinder;
		} else {
			// default implementation of the successor: picks up a successor
			// randomly
			this.successorFinder = new SuccessorFinder<A, S, N>() {
				@Override
				public N estimate(N node, NodeExpander<A, S, N> nodeExpander) {
					List<N> successors = new ArrayList<>();
					// find a random successor
					for (N successor : nodeExpander.expand(node)) {
						successors.add(successor);
					}
					Random randIndGen = new Random();
					return successors.get(Math.abs(randIndGen.nextInt()) % successors.size());
				}
			};
		}
	}

	@Override
	public ASIterator iterator() {
		// TODO Auto-generated method stub
		return new ASIterator();
	}

	public class ASIterator implements Iterator<N> {

		private Queue<N> queue = new LinkedList<N>();
		private Double bestScore = null;
		private Double curTemp = START_TEMP;

		private ASIterator() {
			bestScore = initialNode.getEstimation();
			queue.add(initialNode);
		}

		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}

		@Override
		public N next() {
			N currentNode = this.queue.poll();
			if (curTemp > minTemp) {
				N newNode = null;
				// we add a loop to increase the effect of a change of alpha.
				for (int i = 0; i < 100; i++) {
					N randSuccessor = successorFinder.estimate(currentNode, nodeExpander);
					Double score = randSuccessor.getScore();
					if (acceptanceProbability.compute(bestScore, score, curTemp) > Math.random()) {
						newNode = randSuccessor;
						bestScore = score;
					}
				}
				if (newNode != null) {
					queue.add(newNode);
				} else {
					queue.add(currentNode);
				}
				curTemp *= alpha;
			}
			return currentNode;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();

		}
	}

	/**
	 * Interface to compute the acceptance probability. If the new score is less
	 * than the old score, 1 will be returned so that the node is selected.
	 * Otherwise, we compute a probability that will decrease when the newScore
	 * or the temperature increase.
	 * 
	 */

	public interface AcceptanceProbability {
		Double compute(Double oldScore, Double newScore, Double temp);
	}

	/**
	 * Interface to find the successor of a node.
	 *
	 * @param <N>
	 */
	public interface SuccessorFinder<A, S, N extends Node<A, S, N>> {

		N estimate(N node, NodeExpander<A, S, N> nodeExpander);
	}
}
