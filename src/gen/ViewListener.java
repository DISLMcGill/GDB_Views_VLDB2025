// Generated from src/View.g4 by ANTLR 4.8
package gen;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ViewParser}.
 */
public interface ViewListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ViewParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(ViewParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(ViewParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#scope}.
	 * @param ctx the parse tree
	 */
	void enterScope(ViewParser.ScopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#scope}.
	 * @param ctx the parse tree
	 */
	void exitScope(ViewParser.ScopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(ViewParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(ViewParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#changegraph}.
	 * @param ctx the parse tree
	 */
	void enterChangegraph(ViewParser.ChangegraphContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#changegraph}.
	 * @param ctx the parse tree
	 */
	void exitChangegraph(ViewParser.ChangegraphContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#pipeline}.
	 * @param ctx the parse tree
	 */
	void enterPipeline(ViewParser.PipelineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#pipeline}.
	 * @param ctx the parse tree
	 */
	void exitPipeline(ViewParser.PipelineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#replacements}.
	 * @param ctx the parse tree
	 */
	void enterReplacements(ViewParser.ReplacementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#replacements}.
	 * @param ctx the parse tree
	 */
	void exitReplacements(ViewParser.ReplacementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#caseCondition}.
	 * @param ctx the parse tree
	 */
	void enterCaseCondition(ViewParser.CaseConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#caseCondition}.
	 * @param ctx the parse tree
	 */
	void exitCaseCondition(ViewParser.CaseConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#iteration}.
	 * @param ctx the parse tree
	 */
	void enterIteration(ViewParser.IterationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#iteration}.
	 * @param ctx the parse tree
	 */
	void exitIteration(ViewParser.IterationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#validVal}.
	 * @param ctx the parse tree
	 */
	void enterValidVal(ViewParser.ValidValContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#validVal}.
	 * @param ctx the parse tree
	 */
	void exitValidVal(ViewParser.ValidValContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#iterationCase}.
	 * @param ctx the parse tree
	 */
	void enterIterationCase(ViewParser.IterationCaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#iterationCase}.
	 * @param ctx the parse tree
	 */
	void exitIterationCase(ViewParser.IterationCaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#size}.
	 * @param ctx the parse tree
	 */
	void enterSize(ViewParser.SizeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#size}.
	 * @param ctx the parse tree
	 */
	void exitSize(ViewParser.SizeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(ViewParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(ViewParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#pipeconditions}.
	 * @param ctx the parse tree
	 */
	void enterPipeconditions(ViewParser.PipeconditionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#pipeconditions}.
	 * @param ctx the parse tree
	 */
	void exitPipeconditions(ViewParser.PipeconditionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#pipeexpr}.
	 * @param ctx the parse tree
	 */
	void enterPipeexpr(ViewParser.PipeexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#pipeexpr}.
	 * @param ctx the parse tree
	 */
	void exitPipeexpr(ViewParser.PipeexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(ViewParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(ViewParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#viewuse}.
	 * @param ctx the parse tree
	 */
	void enterViewuse(ViewParser.ViewuseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#viewuse}.
	 * @param ctx the parse tree
	 */
	void exitViewuse(ViewParser.ViewuseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#usedviews}.
	 * @param ctx the parse tree
	 */
	void enterUsedviews(ViewParser.UsedviewsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#usedviews}.
	 * @param ctx the parse tree
	 */
	void exitUsedviews(ViewParser.UsedviewsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#viewatom}.
	 * @param ctx the parse tree
	 */
	void enterViewatom(ViewParser.ViewatomContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#viewatom}.
	 * @param ctx the parse tree
	 */
	void exitViewatom(ViewParser.ViewatomContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#returnstmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnstmt(ViewParser.ReturnstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#returnstmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnstmt(ViewParser.ReturnstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#retval}.
	 * @param ctx the parse tree
	 */
	void enterRetval(ViewParser.RetvalContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#retval}.
	 * @param ctx the parse tree
	 */
	void exitRetval(ViewParser.RetvalContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#constructstmt}.
	 * @param ctx the parse tree
	 */
	void enterConstructstmt(ViewParser.ConstructstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#constructstmt}.
	 * @param ctx the parse tree
	 */
	void exitConstructstmt(ViewParser.ConstructstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#orderstmt}.
	 * @param ctx the parse tree
	 */
	void enterOrderstmt(ViewParser.OrderstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#orderstmt}.
	 * @param ctx the parse tree
	 */
	void exitOrderstmt(ViewParser.OrderstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#orderItem}.
	 * @param ctx the parse tree
	 */
	void enterOrderItem(ViewParser.OrderItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#orderItem}.
	 * @param ctx the parse tree
	 */
	void exitOrderItem(ViewParser.OrderItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#order}.
	 * @param ctx the parse tree
	 */
	void enterOrder(ViewParser.OrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#order}.
	 * @param ctx the parse tree
	 */
	void exitOrder(ViewParser.OrderContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#limitstmt}.
	 * @param ctx the parse tree
	 */
	void enterLimitstmt(ViewParser.LimitstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#limitstmt}.
	 * @param ctx the parse tree
	 */
	void exitLimitstmt(ViewParser.LimitstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(ViewParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(ViewParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(ViewParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(ViewParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(ViewParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(ViewParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#nodeName}.
	 * @param ctx the parse tree
	 */
	void enterNodeName(ViewParser.NodeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#nodeName}.
	 * @param ctx the parse tree
	 */
	void exitNodeName(ViewParser.NodeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(ViewParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(ViewParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#relationValue}.
	 * @param ctx the parse tree
	 */
	void enterRelationValue(ViewParser.RelationValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#relationValue}.
	 * @param ctx the parse tree
	 */
	void exitRelationValue(ViewParser.RelationValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#relationLength}.
	 * @param ctx the parse tree
	 */
	void enterRelationLength(ViewParser.RelationLengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#relationLength}.
	 * @param ctx the parse tree
	 */
	void exitRelationLength(ViewParser.RelationLengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#pathExp}.
	 * @param ctx the parse tree
	 */
	void enterPathExp(ViewParser.PathExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#pathExp}.
	 * @param ctx the parse tree
	 */
	void exitPathExp(ViewParser.PathExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(ViewParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(ViewParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#conditions}.
	 * @param ctx the parse tree
	 */
	void enterConditions(ViewParser.ConditionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#conditions}.
	 * @param ctx the parse tree
	 */
	void exitConditions(ViewParser.ConditionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#boolexpr}.
	 * @param ctx the parse tree
	 */
	void enterBoolexpr(ViewParser.BoolexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#boolexpr}.
	 * @param ctx the parse tree
	 */
	void exitBoolexpr(ViewParser.BoolexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(ViewParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(ViewParser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#val}.
	 * @param ctx the parse tree
	 */
	void enterVal(ViewParser.ValContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#val}.
	 * @param ctx the parse tree
	 */
	void exitVal(ViewParser.ValContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#indexing}.
	 * @param ctx the parse tree
	 */
	void enterIndexing(ViewParser.IndexingContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#indexing}.
	 * @param ctx the parse tree
	 */
	void exitIndexing(ViewParser.IndexingContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#setattr}.
	 * @param ctx the parse tree
	 */
	void enterSetattr(ViewParser.SetattrContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#setattr}.
	 * @param ctx the parse tree
	 */
	void exitSetattr(ViewParser.SetattrContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#arithmetic}.
	 * @param ctx the parse tree
	 */
	void enterArithmetic(ViewParser.ArithmeticContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#arithmetic}.
	 * @param ctx the parse tree
	 */
	void exitArithmetic(ViewParser.ArithmeticContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#insertion}.
	 * @param ctx the parse tree
	 */
	void enterInsertion(ViewParser.InsertionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#insertion}.
	 * @param ctx the parse tree
	 */
	void exitInsertion(ViewParser.InsertionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#insertrelation}.
	 * @param ctx the parse tree
	 */
	void enterInsertrelation(ViewParser.InsertrelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#insertrelation}.
	 * @param ctx the parse tree
	 */
	void exitInsertrelation(ViewParser.InsertrelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#insertionVar}.
	 * @param ctx the parse tree
	 */
	void enterInsertionVar(ViewParser.InsertionVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#insertionVar}.
	 * @param ctx the parse tree
	 */
	void exitInsertionVar(ViewParser.InsertionVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#insertAttributes}.
	 * @param ctx the parse tree
	 */
	void enterInsertAttributes(ViewParser.InsertAttributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#insertAttributes}.
	 * @param ctx the parse tree
	 */
	void exitInsertAttributes(ViewParser.InsertAttributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#as}.
	 * @param ctx the parse tree
	 */
	void enterAs(ViewParser.AsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#as}.
	 * @param ctx the parse tree
	 */
	void exitAs(ViewParser.AsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ViewParser#exists}.
	 * @param ctx the parse tree
	 */
	void enterExists(ViewParser.ExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ViewParser#exists}.
	 * @param ctx the parse tree
	 */
	void exitExists(ViewParser.ExistsContext ctx);
}
