package com.vladsch.flexmark.internal;

import com.vladsch.flexmark.ast.BlockContent;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.parser.InlineParser;
import com.vladsch.flexmark.parser.block.AbstractBlockParser;
import com.vladsch.flexmark.parser.block.BlockContinue;
import com.vladsch.flexmark.parser.block.ParserState;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public class ParagraphParser extends AbstractBlockParser {

    private final Paragraph block = new Paragraph();
    private BlockContent content = new BlockContent();

    public BlockContent getBlockContent() {
        return content;
    }

    @Override
    public Paragraph getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState state) {
        if (!state.isBlank()) {
            return BlockContinue.atIndex(state.getIndex());
        } else {
            block.setTrailingBlankLine(true);
            return BlockContinue.none();
        }
    }

    @Override
    public void addLine(ParserState state, BasedSequence line) {
        content.add(line, state.getIndent());
    }

    @Override
    public boolean isParagraphParser() {
        return true;
    }

    @Override
    public void closeBlock(ParserState state) {
        block.setContent(content);
        content = null;
    }

    @Override
    public void parseInlines(InlineParser inlineParser) {
        inlineParser.parse(getBlock().getContentChars(), getBlock());
    }
}
