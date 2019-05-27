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
	
	public void print(Node ast) {
		System.err.println(ast.getClass());
	}

}