// Generated from C:/Users/Ting/Documents/GDBViews/src\View.g4 by ANTLR 4.8
package gen;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ViewParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ViewVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ViewParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(ViewParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScope(ViewParser.ScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(ViewParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#changegraph}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChangegraph(ViewParser.ChangegraphContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#pipeline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipeline(ViewParser.PipelineContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#replacements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplacements(ViewParser.ReplacementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#iteration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIteration(ViewParser.IterationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#validVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidVal(ViewParser.ValidValContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#iterationCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterationCase(ViewParser.IterationCaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#size}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSize(ViewParser.SizeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(ViewParser.RangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#pipeconditions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipeconditions(ViewParser.PipeconditionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#pipeexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipeexpr(ViewParser.PipeexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(ViewParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#viewuse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewuse(ViewParser.ViewuseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#usedviews}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsedviews(ViewParser.UsedviewsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#viewatom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewatom(ViewParser.ViewatomContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#returnstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnstmt(ViewParser.ReturnstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#retval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetval(ViewParser.RetvalContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(ViewParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(ViewParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(ViewParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#nodeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeName(ViewParser.NodeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(ViewParser.RelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#relationValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationValue(ViewParser.RelationValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(ViewParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#conditions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditions(ViewParser.ConditionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#boolexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolexpr(ViewParser.BoolexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(ViewParser.AttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#val}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVal(ViewParser.ValContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#indexing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexing(ViewParser.IndexingContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#setattr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetattr(ViewParser.SetattrContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#arithmetic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmetic(ViewParser.ArithmeticContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#insertion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertion(ViewParser.InsertionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#insertrelation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertrelation(ViewParser.InsertrelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#insertionVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertionVar(ViewParser.InsertionVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#insertAttributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertAttributes(ViewParser.InsertAttributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#as}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAs(ViewParser.AsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ViewParser#exists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExists(ViewParser.ExistsContext ctx);
}