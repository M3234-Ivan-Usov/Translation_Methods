// Generated from src/ru/ifmo/rain/usov/antlr/grammar/Python.g4 by ANTLR 4.8
package ru.ifmo.rain.usov.antlr.grammar;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PythonParser}.
 */
public interface PythonListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PythonParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(PythonParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(PythonParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(PythonParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(PythonParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#complex_statement}.
	 * @param ctx the parse tree
	 */
	void enterComplex_statement(PythonParser.Complex_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#complex_statement}.
	 * @param ctx the parse tree
	 */
	void exitComplex_statement(PythonParser.Complex_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#single_line}.
	 * @param ctx the parse tree
	 */
	void enterSingle_line(PythonParser.Single_lineContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#single_line}.
	 * @param ctx the parse tree
	 */
	void exitSingle_line(PythonParser.Single_lineContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(PythonParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(PythonParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#func_def}.
	 * @param ctx the parse tree
	 */
	void enterFunc_def(PythonParser.Func_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#func_def}.
	 * @param ctx the parse tree
	 */
	void exitFunc_def(PythonParser.Func_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#func_params}.
	 * @param ctx the parse tree
	 */
	void enterFunc_params(PythonParser.Func_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#func_params}.
	 * @param ctx the parse tree
	 */
	void exitFunc_params(PythonParser.Func_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(PythonParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(PythonParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#typed_arg}.
	 * @param ctx the parse tree
	 */
	void enterTyped_arg(PythonParser.Typed_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#typed_arg}.
	 * @param ctx the parse tree
	 */
	void exitTyped_arg(PythonParser.Typed_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#key_arg}.
	 * @param ctx the parse tree
	 */
	void enterKey_arg(PythonParser.Key_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#key_arg}.
	 * @param ctx the parse tree
	 */
	void exitKey_arg(PythonParser.Key_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#simple_arg}.
	 * @param ctx the parse tree
	 */
	void enterSimple_arg(PythonParser.Simple_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#simple_arg}.
	 * @param ctx the parse tree
	 */
	void exitSimple_arg(PythonParser.Simple_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#func_body}.
	 * @param ctx the parse tree
	 */
	void enterFunc_body(PythonParser.Func_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#func_body}.
	 * @param ctx the parse tree
	 */
	void exitFunc_body(PythonParser.Func_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#clazz}.
	 * @param ctx the parse tree
	 */
	void enterClazz(PythonParser.ClazzContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#clazz}.
	 * @param ctx the parse tree
	 */
	void exitClazz(PythonParser.ClazzContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#ancestors}.
	 * @param ctx the parse tree
	 */
	void enterAncestors(PythonParser.AncestorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#ancestors}.
	 * @param ctx the parse tree
	 */
	void exitAncestors(PythonParser.AncestorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#clazz_body}.
	 * @param ctx the parse tree
	 */
	void enterClazz_body(PythonParser.Clazz_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#clazz_body}.
	 * @param ctx the parse tree
	 */
	void exitClazz_body(PythonParser.Clazz_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(PythonParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(PythonParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#update}.
	 * @param ctx the parse tree
	 */
	void enterUpdate(PythonParser.UpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#update}.
	 * @param ctx the parse tree
	 */
	void exitUpdate(PythonParser.UpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#ret}.
	 * @param ctx the parse tree
	 */
	void enterRet(PythonParser.RetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#ret}.
	 * @param ctx the parse tree
	 */
	void exitRet(PythonParser.RetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#import_lib}.
	 * @param ctx the parse tree
	 */
	void enterImport_lib(PythonParser.Import_libContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#import_lib}.
	 * @param ctx the parse tree
	 */
	void exitImport_lib(PythonParser.Import_libContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#func_call}.
	 * @param ctx the parse tree
	 */
	void enterFunc_call(PythonParser.Func_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#func_call}.
	 * @param ctx the parse tree
	 */
	void exitFunc_call(PythonParser.Func_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#built_in}.
	 * @param ctx the parse tree
	 */
	void enterBuilt_in(PythonParser.Built_inContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#built_in}.
	 * @param ctx the parse tree
	 */
	void exitBuilt_in(PythonParser.Built_inContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#call_params}.
	 * @param ctx the parse tree
	 */
	void enterCall_params(PythonParser.Call_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#call_params}.
	 * @param ctx the parse tree
	 */
	void exitCall_params(PythonParser.Call_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(PythonParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(PythonParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#key_param}.
	 * @param ctx the parse tree
	 */
	void enterKey_param(PythonParser.Key_paramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#key_param}.
	 * @param ctx the parse tree
	 */
	void exitKey_param(PythonParser.Key_paramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#simple_param}.
	 * @param ctx the parse tree
	 */
	void enterSimple_param(PythonParser.Simple_paramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#simple_param}.
	 * @param ctx the parse tree
	 */
	void exitSimple_param(PythonParser.Simple_paramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterLvalue(PythonParser.LvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitLvalue(PythonParser.LvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#rvalue}.
	 * @param ctx the parse tree
	 */
	void enterRvalue(PythonParser.RvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#rvalue}.
	 * @param ctx the parse tree
	 */
	void exitRvalue(PythonParser.RvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#for_cycle}.
	 * @param ctx the parse tree
	 */
	void enterFor_cycle(PythonParser.For_cycleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#for_cycle}.
	 * @param ctx the parse tree
	 */
	void exitFor_cycle(PythonParser.For_cycleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#arithmetic_cycle}.
	 * @param ctx the parse tree
	 */
	void enterArithmetic_cycle(PythonParser.Arithmetic_cycleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#arithmetic_cycle}.
	 * @param ctx the parse tree
	 */
	void exitArithmetic_cycle(PythonParser.Arithmetic_cycleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#range_based}.
	 * @param ctx the parse tree
	 */
	void enterRange_based(PythonParser.Range_basedContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#range_based}.
	 * @param ctx the parse tree
	 */
	void exitRange_based(PythonParser.Range_basedContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#int_value}.
	 * @param ctx the parse tree
	 */
	void enterInt_value(PythonParser.Int_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#int_value}.
	 * @param ctx the parse tree
	 */
	void exitInt_value(PythonParser.Int_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#while_cycle}.
	 * @param ctx the parse tree
	 */
	void enterWhile_cycle(PythonParser.While_cycleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#while_cycle}.
	 * @param ctx the parse tree
	 */
	void exitWhile_cycle(PythonParser.While_cycleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#cycle}.
	 * @param ctx the parse tree
	 */
	void enterCycle(PythonParser.CycleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#cycle}.
	 * @param ctx the parse tree
	 */
	void exitCycle(PythonParser.CycleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#if_condition}.
	 * @param ctx the parse tree
	 */
	void enterIf_condition(PythonParser.If_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#if_condition}.
	 * @param ctx the parse tree
	 */
	void exitIf_condition(PythonParser.If_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#elif_condition}.
	 * @param ctx the parse tree
	 */
	void enterElif_condition(PythonParser.Elif_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#elif_condition}.
	 * @param ctx the parse tree
	 */
	void exitElif_condition(PythonParser.Elif_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#else_condition}.
	 * @param ctx the parse tree
	 */
	void enterElse_condition(PythonParser.Else_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#else_condition}.
	 * @param ctx the parse tree
	 */
	void exitElse_condition(PythonParser.Else_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#branch}.
	 * @param ctx the parse tree
	 */
	void enterBranch(PythonParser.BranchContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#branch}.
	 * @param ctx the parse tree
	 */
	void exitBranch(PythonParser.BranchContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(PythonParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(PythonParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#comp_or}.
	 * @param ctx the parse tree
	 */
	void enterComp_or(PythonParser.Comp_orContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#comp_or}.
	 * @param ctx the parse tree
	 */
	void exitComp_or(PythonParser.Comp_orContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#comp_or_prime}.
	 * @param ctx the parse tree
	 */
	void enterComp_or_prime(PythonParser.Comp_or_primeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#comp_or_prime}.
	 * @param ctx the parse tree
	 */
	void exitComp_or_prime(PythonParser.Comp_or_primeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#comp_xor}.
	 * @param ctx the parse tree
	 */
	void enterComp_xor(PythonParser.Comp_xorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#comp_xor}.
	 * @param ctx the parse tree
	 */
	void exitComp_xor(PythonParser.Comp_xorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#comp_xor_prime}.
	 * @param ctx the parse tree
	 */
	void enterComp_xor_prime(PythonParser.Comp_xor_primeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#comp_xor_prime}.
	 * @param ctx the parse tree
	 */
	void exitComp_xor_prime(PythonParser.Comp_xor_primeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#comp_and}.
	 * @param ctx the parse tree
	 */
	void enterComp_and(PythonParser.Comp_andContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#comp_and}.
	 * @param ctx the parse tree
	 */
	void exitComp_and(PythonParser.Comp_andContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#comp_and_prime}.
	 * @param ctx the parse tree
	 */
	void enterComp_and_prime(PythonParser.Comp_and_primeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#comp_and_prime}.
	 * @param ctx the parse tree
	 */
	void exitComp_and_prime(PythonParser.Comp_and_primeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#comp_not}.
	 * @param ctx the parse tree
	 */
	void enterComp_not(PythonParser.Comp_notContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#comp_not}.
	 * @param ctx the parse tree
	 */
	void exitComp_not(PythonParser.Comp_notContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#arithmetic_comparison}.
	 * @param ctx the parse tree
	 */
	void enterArithmetic_comparison(PythonParser.Arithmetic_comparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#arithmetic_comparison}.
	 * @param ctx the parse tree
	 */
	void exitArithmetic_comparison(PythonParser.Arithmetic_comparisonContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#arithmetic}.
	 * @param ctx the parse tree
	 */
	void enterArithmetic(PythonParser.ArithmeticContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#arithmetic}.
	 * @param ctx the parse tree
	 */
	void exitArithmetic(PythonParser.ArithmeticContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(PythonParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(PythonParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(PythonParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(PythonParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#power}.
	 * @param ctx the parse tree
	 */
	void enterPower(PythonParser.PowerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#power}.
	 * @param ctx the parse tree
	 */
	void exitPower(PythonParser.PowerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(PythonParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(PythonParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#complex_name}.
	 * @param ctx the parse tree
	 */
	void enterComplex_name(PythonParser.Complex_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#complex_name}.
	 * @param ctx the parse tree
	 */
	void exitComplex_name(PythonParser.Complex_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(PythonParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(PythonParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#support_types}.
	 * @param ctx the parse tree
	 */
	void enterSupport_types(PythonParser.Support_typesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#support_types}.
	 * @param ctx the parse tree
	 */
	void exitSupport_types(PythonParser.Support_typesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#bool}.
	 * @param ctx the parse tree
	 */
	void enterBool(PythonParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#bool}.
	 * @param ctx the parse tree
	 */
	void exitBool(PythonParser.BoolContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#none}.
	 * @param ctx the parse tree
	 */
	void enterNone(PythonParser.NoneContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#none}.
	 * @param ctx the parse tree
	 */
	void exitNone(PythonParser.NoneContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#floating}.
	 * @param ctx the parse tree
	 */
	void enterFloating(PythonParser.FloatingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#floating}.
	 * @param ctx the parse tree
	 */
	void exitFloating(PythonParser.FloatingContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#integral}.
	 * @param ctx the parse tree
	 */
	void enterIntegral(PythonParser.IntegralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#integral}.
	 * @param ctx the parse tree
	 */
	void exitIntegral(PythonParser.IntegralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(PythonParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(PythonParser.StringContext ctx);
}