/*
 * Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package hipster.model.problem;


import hipster.model.Transition;
import hipster.model.function.ActionFunction;
import hipster.model.function.ActionStateTransitionFunction;
import hipster.model.function.CostFunction;
import hipster.model.function.HeuristicFunction;
import hipster.model.function.NodeExpander;
import hipster.model.function.NodeFactory;
import hipster.model.function.TransitionFunction;
import hipster.model.function.impl.BinaryOperation;
import hipster.model.function.impl.LazyActionStateTransitionFunction;
import hipster.model.function.impl.LazyNodeExpander;
import hipster.model.function.impl.WeightedNodeFactory;
import hipster.model.impl.UnweightedNode;
import hipster.model.impl.WeightedNode;

/**
 * Problem builder that is used to guide the user through the creation of a
 * {@link SearchProblem} with the main components
 * required to instantiate an algorithm.
 */
public final class ProblemBuilder {

    private ProblemBuilder(){

    }

    /**
     * Internal wizard assistant class.
     */
    public static final class Wizard {
        private Wizard(){}

        /**
         * Step to define the initial state of the problem.
         */
        public static final class ActionState<S> {
            private final S initialState;
            private final S finalState;

            public ActionState(S initialState) {
                this.initialState = initialState;
                this.finalState = null;
            }

            public ActionState(S initialState, S finalState) {
                this.initialState = initialState;
                this.finalState = finalState;
            }


            public WithAction defineProblemWithExplicitActions(){
                return new WithAction();
            }

            public WithoutAction defineProblemWithoutActions(){
                return new WithoutAction();
            }

            /**
             * Builder step to define a search problem without actions.
             */
            public final class WithoutAction {
                private WithoutAction(){}



                public Uninformed<Void> useTransitionFunction(TransitionFunction<Void, S> transitionFunction){
                    return new Uninformed<Void>(transitionFunction);
                }
            }

            /**
             * Builder step to define a search problem with actions.
             */
            public final class WithAction {
                private WithAction(){}

                /**
                 * Select the action function that returns the applicable actions for
                 * each state in your problem.
                 * @param actionFunction action function to be used.
                 */
                public <A> Action<A> useActionFunction(ActionFunction<A, S> actionFunction){
                    return new Action<A>(actionFunction);
                }

                /**
                 * Builder step to select the transition function of a action-explicit search problem.
                 */
                public final class Action<A> {
                    private ActionFunction<A, S> af;

                    public Action(ActionFunction<A, S> af) {
                        this.af = af;
                    }


                    public Uninformed<A> useTransitionFunction(ActionStateTransitionFunction<A, S> atf){
                        return new Uninformed<A>(new LazyActionStateTransitionFunction<A, S>(af, atf));
                    }
                }


                public <A> Uninformed<A> useTransitionFunction(TransitionFunction<A, S> transitionFunction){
                    return new Uninformed<A>(transitionFunction);
                }
            }


            public final class Uninformed<A> {
                private final TransitionFunction<A,S> tf;

                private Uninformed(TransitionFunction<A, S> tf){
                    this.tf = tf;
                }

                public SearchProblem<A, S, UnweightedNode<A, S>> build(){
                    NodeFactory<A,S,UnweightedNode<A,S>> factory = new NodeFactory<A, S, UnweightedNode<A, S>>() {
                        @Override
                        public UnweightedNode<A, S> makeNode(UnweightedNode<A, S> fromNode, Transition<A, S> transition) {
                            return new UnweightedNode<A, S>(fromNode, transition);
                        }
                    };
                    UnweightedNode<A,S> initialNode = factory.makeNode(null, Transition.<A, S>create(null, null, initialState));
                    UnweightedNode<A,S> finalNode = factory.makeNode(null, Transition.<A, S>create(null, null, finalState));
                    NodeExpander<A,S,UnweightedNode<A,S>> nodeExpander = new LazyNodeExpander<A, S, UnweightedNode<A, S>>(tf, factory);
                    return new SearchProblem<A,S, UnweightedNode<A,S>>(initialNode, finalNode, nodeExpander);
                }

                /**
                 * Define a cost function. This immediately defines an InformedSearchProblem
                 * @param cf
                 *
                 */
                public Informed<Double> useCostFunction(CostFunction<A, S, Double> cf){
                    // Create default components
                    return new Informed<Double>(cf, BinaryOperation.doubleAdditionOp());
                }

                public <C extends Comparable<C>> Informed<C> useGenericCostFunction(CostFunction<A,S,C> cf, BinaryOperation<C> costAlgebra){
                    return new Informed<C>(cf, costAlgebra);
                }

                /**
                 * An informed search problem builder generates informed search problems with a generic cost
                 */
                public final class Informed<C extends Comparable<C>> {
                    private CostFunction<A,S,C> cf;
                    private BinaryOperation<C> costAlgebra;

                    public Informed(CostFunction<A, S, C> cf, BinaryOperation<C> costAlgebra) {
                        this.cf = cf;
                        this.costAlgebra = costAlgebra;
                    }

                    public SearchProblem<A, S, WeightedNode<A, S, C>> build(){
                        WeightedNodeFactory<A,S,C> factory = new WeightedNodeFactory<A,S,C>(
                                cf,
                                new HeuristicFunction<S, C>() {
                                    @Override
                                    public C estimate(S state) {
                                        return costAlgebra.getIdentityElem();
                                    }
                                }, costAlgebra);
                        // Make the initial node. The initial node contains the initial state
                        // of the problem, and it comes from no previous node (null) and using no action (null)
                        WeightedNode<A,S,C> initialNode = factory.makeNode(null, Transition.<A,S>create(null, null, initialState));
                        // Create a Lazy Node Expander by default
                        NodeExpander<A,S,WeightedNode<A,S,C>> expander = new LazyNodeExpander<A, S, WeightedNode<A, S, C>>(tf, factory);
                        // Create the algorithm with all those components
                        return new SearchProblem<A,S,WeightedNode<A,S,C>>(initialNode, expander);
                    }

                    public Heuristic useHeuristicFunction(HeuristicFunction<S, C> hf){
                        return new Heuristic(hf);
                    }

                    /**
                     * Defines the heuristic function to be used.
                     */
                    public final class Heuristic {
                        private HeuristicFunction<S,C> hf;

                        public Heuristic(HeuristicFunction<S, C> hf) {
                            this.hf = hf;
                        }

                        public SearchProblem<A, S, WeightedNode<A, S, C>> build(){
                            WeightedNodeFactory<A, S, C> factory = new WeightedNodeFactory<A,S,C>(
                                    cf, hf, costAlgebra);
                            WeightedNode<A,S,C> initialNode = factory.makeNode(null, Transition.<A,S>create(null, null, initialState));
                            LazyNodeExpander<A, S, WeightedNode<A, S, C>> nodeExpander =
                                    new LazyNodeExpander<A, S, WeightedNode<A, S, C>>(tf, factory);

                            return new SearchProblem<A, S, WeightedNode<A,S,C>>(initialNode, nodeExpander);
                        }
                    }
                }
            }
        }

        public <S> ActionState<S> initialState(S initialState){
            return new ActionState<S>(initialState);
        }

        public <S> ActionState<S> initialState(S initialState, S finalState){
            return new ActionState<S>(initialState, finalState);
        }

    }

    /**
     * <p>
     * Creates the builder. Chain method calls until you have the problem
     * ready to call {@code build()}. Example usage:
     * </p>
     * <pre class="prettyprint">
     *     {@code
     *     Hipster.SearchProblem p =
     *          ProblemBuilder.create()
     *              .initialState(initialState)
     *              .defineProblemWithExplicitActions()
     *                  .useActionFunction(af)
     *                  .useTransitionFunction(atf)
     *                  .useCostFunction(cf)
     *                  .useHeuristicFunction(hf)
     *                  .build();
     *     }
     * </pre>
     */
    public static Wizard create(){
        return new Wizard();
    }
}
