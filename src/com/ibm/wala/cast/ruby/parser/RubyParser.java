package com.ibm.wala.cast.ruby.parser;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Supplier;
import org.jrubyparser.CompatVersion;
import org.jrubyparser.Parser;
import org.jrubyparser.ast.*;
import org.jrubyparser.NodeVisitor;
import org.jrubyparser.parser.ParserConfiguration;


import com.ibm.wala.cast.ir.translator.AbstractClassEntity;
import com.ibm.wala.cast.ir.translator.AbstractCodeEntity;
import com.ibm.wala.cast.ir.translator.AbstractFieldEntity;
import com.ibm.wala.cast.ir.translator.AbstractScriptEntity;
import com.ibm.wala.cast.ir.translator.TranslatorToCAst;

import com.ibm.wala.cast.tree.CAst;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.cast.tree.CAstQualifier;
import com.ibm.wala.cast.tree.CAstSourcePositionMap.Position;
import com.ibm.wala.cast.tree.CAstType;
import com.ibm.wala.cast.tree.impl.AbstractSourcePosition;
import com.ibm.wala.cast.tree.impl.CAstControlFlowRecorder;
import com.ibm.wala.cast.tree.impl.CAstImpl;
import com.ibm.wala.cast.tree.impl.CAstNodeTypeMapRecorder;
import com.ibm.wala.cast.tree.impl.CAstOperator;
import com.ibm.wala.cast.tree.impl.CAstSourcePositionRecorder;
import com.ibm.wala.cast.tree.impl.CAstSymbolImpl;
import com.ibm.wala.cast.tree.impl.CAstTypeDictionaryImpl;
import com.ibm.wala.cast.tree.rewrite.CAstRewriter.CopyKey;
import com.ibm.wala.cast.tree.rewrite.CAstRewriter.RewriteContext;
import com.ibm.wala.cast.tree.rewrite.CAstRewriterFactory;
import com.ibm.wala.classLoader.Module;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.collections.HashMapFactory;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.collections.ReverseIterator;
import com.ibm.wala.util.warnings.Warning;

abstract public class RubyParser<T> implements TranslatorToCAst {

	
	private CAstType codeBody = new CAstType() {

		@Override
		public String getName() {
			return "CodeBody";
		}

		@Override
		public Collection<CAstType> getSupertypes() {
			return Collections.emptySet();
		}
		
	};

	interface WalkContext extends TranslatorToCAst.WalkContext<WalkContext, RootNode> {
 
		WalkContext getParent();
		
		default CAstEntity entity() {
			return getParent().entity();
		}
		
	}
	private static class RootContext extends TranslatorToCAst.RootContext<WalkContext, RootNode> implements WalkContext {
		private final Module ast;
		
		private RootContext(Module ast) { 
			this.ast = ast;
		}
		
//		@Override
//		public RootNode top() {
//			return (RootNode) ast;
//		}

		public WalkContext getParent() {
			assert false;
			return null;
		}
	}

	private final CAst Ast = new CAstImpl();
	private class CAstVisitor implements NodeVisitor<CAstNode> {
		private final RubyParser.WalkContext context;
		private final WalaRubyParser parser;
		
		private CAstVisitor(RubyParser.WalkContext context, WalaRubyParser parser) {
			this.context = context;
			this.parser = parser;
		}
		private Position makePosition(Node p) {
			String s = parser.getText(p.getPosition().getStartOffset(), p.getPosition().getEndOffset());
			String[] lines = s.split("\n");
			int last_col;
			int last_line = p.getPosition().getStartLine() + lines.length - 1;
			if ("".equals(s) || lines.length <= 1) {
				last_col = p.getPosition().getStartLine() + (p.getPosition().getEndOffset() - p.getPosition().getStartOffset());
			} else {
				assert (lines.length > 1);
				last_col = lines[lines.length-1].length();
			} 

			return new AbstractSourcePosition() {

				@Override
				public URL getURL() {
					try {
						return getParsedURL();
					} catch (IOException e) {
						assert false : e;
						return null;
					}
				}

	
				@Override
				public int getFirstLine() {
					return p.getPosition().getStartLine();
				}

				@Override
				public int getFirstCol() {
					return p.getPosition().getStartOffset();
				}
				
				@Override
				public int getLastLine() {
					return last_line;
				}

				@Override
				public int getLastCol() {
					return last_col;
				}

				@Override
				public int getFirstOffset() {
					return p.getPosition().getStartOffset();
				}

				@Override
				public int getLastOffset() {
					return p.getPosition().getEndOffset();
				}



				@Override
				public Reader getReader() throws IOException {
					// TODO Auto-generated method stub
					return null;
				}
				
			};
		}
		private CAstNode fail(RootNode tree) {
// pretend it is a no-op for now.
//			assert false : tree;
			return Ast.makeNode(CAstNode.EMPTY);
		}
		@Override
		public CAstNode visitAliasNode(AliasNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitAndNode(AndNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitArgsCatNode(ArgsCatNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitArgsNode(ArgsNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitArgsPushNode(ArgsPushNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitArgumentNode(ArgumentNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitArrayNode(ArrayNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitAttrAssignNode(AttrAssignNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitBackRefNode(BackRefNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitBeginNode(BeginNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitBignumNode(BignumNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitBlockArg18Node(BlockArg18Node arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitBlockArgNode(BlockArgNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitBlockNode(BlockNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitBlockPassNode(BlockPassNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitBreakNode(BreakNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitCallNode(CallNode arg0) {
			// TODO Auto-generated method stub
			int i = 0; 
			CAstNode args[] = new CAstNode[1]; // to do set the size correctly
			args[i++] = Ast.makeNode(CAstNode.EMPTY);
			
			return null;
		}

		@Override
		public CAstNode visitCaseNode(CaseNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitClassNode(ClassNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitClassVarAsgnNode(ClassVarAsgnNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitClassVarDeclNode(ClassVarDeclNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitClassVarNode(ClassVarNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitColon2Node(Colon2Node arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitColon3Node(Colon3Node arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitCommentNode(CommentNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitComplexNode(ComplexNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitConstDeclNode(ConstDeclNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitConstNode(ConstNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitDAsgnNode(DAsgnNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitDRegxNode(DRegexpNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitDStrNode(DStrNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitDSymbolNode(DSymbolNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitDVarNode(DVarNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitDXStrNode(DXStrNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitDefinedNode(DefinedNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitDefnNode(DefnNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitDefsNode(DefsNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitDotNode(DotNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitEncodingNode(EncodingNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitEnsureNode(EnsureNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitEvStrNode(EvStrNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitFCallNode(FCallNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitFalseNode(FalseNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitFixnumNode(FixnumNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitFlipNode(FlipNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitFloatNode(FloatNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitForNode(ForNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitGlobalAsgnNode(GlobalAsgnNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitGlobalVarNode(GlobalVarNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitHashNode(HashNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitIfNode(IfNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitImplicitNilNode(ImplicitNilNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitInstAsgnNode(InstAsgnNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitInstVarNode(InstVarNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitIterNode(IterNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitKeywordArgNode(KeywordArgNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitKeywordRestArgNode(KeywordRestArgNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitLambdaNode(LambdaNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitListNode(ListNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitLiteralNode(LiteralNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitLocalAsgnNode(LocalAsgnNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitLocalVarNode(LocalVarNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitMatch2Node(Match2Node arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitMatch3Node(Match3Node arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitMatchNode(MatchNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitMethodNameNode(MethodNameNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitModuleNode(ModuleNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitMultipleAsgnNode(MultipleAsgnNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitNewlineNode(NewlineNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitNextNode(NextNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitNilNode(NilNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitNotNode(NotNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitNthRefNode(NthRefNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitOpAsgnAndNode(OpAsgnAndNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitOpAsgnNode(OpAsgnNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitOpAsgnOrNode(OpAsgnOrNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitOpElementAsgnNode(OpElementAsgnNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitOptArgNode(OptArgNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitOrNode(OrNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitPostExeNode(PostExeNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitPreExeNode(PreExeNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitRationalNode(RationalNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitRedoNode(RedoNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitRegexpNode(RegexpNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitRequiredKeywordArgumentValueNode(RequiredKeywordArgumentValueNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitRescueBodyNode(RescueBodyNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitRescueNode(RescueNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitRestArgNode(RestArgNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitRetryNode(RetryNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitReturnNode(ReturnNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitRootNode(RootNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitSClassNode(SClassNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitSValueNode(SValueNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitSelfNode(SelfNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitSplatNode(SplatNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitStrNode(StrNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitSuperNode(SuperNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitSymbolNode(SymbolNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitSyntaxNode(SyntaxNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitToAryNode(ToAryNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitTrueNode(TrueNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitUnaryCallNode(UnaryCallNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitUndefNode(UndefNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitUntilNode(UntilNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitVAliasNode(VAliasNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitVCallNode(VCallNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitWhenNode(WhenNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitWhileNode(WhileNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitXStrNode(XStrNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitYieldNode(YieldNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitZArrayNode(ZArrayNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CAstNode visitZSuperNode(ZSuperNode arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	protected abstract WalaRubyParser makeParser() throws IOException;
	
	protected abstract Reader getReader() throws IOException;
	
	protected abstract String scriptName();

	protected abstract URL getParsedURL() throws IOException;
//
//	private final CAstTypeDictionaryImpl<String> types;
//	
//	protected Parser(CAstTypeDictionaryImpl<String> types) {
//		this.types = types;
//	}
	
	public void print(Node ast) {
		System.err.println(ast.getClass());
	}

}