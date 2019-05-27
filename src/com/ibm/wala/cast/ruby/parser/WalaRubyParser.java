package com.ibm.wala.cast.ruby.parser;

import java.util.Set;
import org.jrubyparser.CompatVersion;
import org.jrubyparser.Parser;
import org.jrubyparser.ast.*;
import org.jrubyparser.parser.ParserConfiguration;

import com.ibm.wala.util.collections.HashSetFactory;

import java.io.StringReader;
import java.util.List;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenRewriteStream;

public class WalaRubyParser  {

	private String text = null;
	private final Set<Exception> errors = HashSetFactory.make();
	
	public WalaRubyParser(CharStream stream, String filename, String encoding) {
		
	}

	private TokenRewriteStream tokens;
	
	public void recordError(Exception e) {
		errors.add(e);
	}
	
	public Set<Exception> getErrors() {
		return errors; 
	}
	
	public String getText(int start, int end) {
		if (text == null) {
			text = tokens.toOriginalString();
		}
		int e = Math.min(end, text.length()-1);
		if (start >= e) {
			return "";
		} else {
			return text.substring(start, e);
		}
	}

    protected Parser setupParser(boolean single) {
    	 
        Parser rubyParser = new Parser();
//        StringReader in = new StringReader(string);
//        CompatVersion version = CompatVersion.RUBY1_8;
//        ParserConfiguration config = new ParserConfiguration(0, version);
        return rubyParser;
        
    }

}
