package org.commonmark.extras.test;

import org.commonmark.Parser;
import org.commonmark.extras.tables.TableBlockParser;
import org.commonmark.extras.tables.TableHtmlRenderer;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.Node;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TableTest {

    @Test
    public void mustHaveHeaderAndSeparator() {
        assertRendering("Abc|Def", "<p>Abc|Def</p>\n");
        assertRendering("Abc | Def", "<p>Abc | Def</p>\n");
    }

    @Test
    public void separatorMustBeThreeOrMore() {
        assertRendering("Abc|Def\n-|-", "<p>Abc|Def\n-|-</p>\n");
        assertRendering("Abc|Def\n--|--", "<p>Abc|Def\n--|--</p>\n");
    }

    @Test
    public void separatorCanNotHaveLeadingSpaceThenPipe() {
        assertRendering("Abc|Def\n |---|---", "<p>Abc|Def\n|---|---</p>\n");
    }

    @Test
    public void oneHeaderNoBody() {
        assertRendering("Abc|Def\n---|---", "<table>\n" +
                "<thead>\n" +
                "<tr><th>Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody></tbody>\n" +
                "</table>\n");
    }

    @Test
    public void oneHeaderOneBody() {
        assertRendering("Abc|Def\n---|---\n1|2", "<table>\n" +
                "<thead>\n" +
                "<tr><th>Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td>1</td><td>2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void padding() {
        assertRendering(" Abc  | Def \n --- | --- \n 1 | 2 ", "<table>\n" +
                "<thead>\n" +
                "<tr><th>Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td>1</td><td>2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void paddingWithCodeBlockIndentation() {
        assertRendering("Abc|Def\n---|---\n    1|2", "<table>\n" +
                "<thead>\n" +
                "<tr><th>Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td>1</td><td>2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void pipesOnOutside() {
        assertRendering("|Abc|Def|\n|---|---|\n|1|2|", "<table>\n" +
                "<thead>\n" +
                "<tr><th>Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td>1</td><td>2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void inlineElements() {
        assertRendering("*Abc*|Def\n---|---\n1|2", "<table>\n" +
                "<thead>\n" +
                "<tr><th><em>Abc</em></th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td>1</td><td>2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void insideBlockQuote() {
        assertRendering("> Abc|Def\n> ---|---\n> 1|2", "<blockquote>\n" +
                "<table>\n" +
                "<thead>\n" +
                "<tr><th>Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td>1</td><td>2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</blockquote>\n");
    }

    @Test
    public void escapedPipe() {
        assertRendering("Abc|Def\n---|---\n1\\|2|20", "<table>\n" +
                "<thead>\n" +
                "<tr><th>Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td>1|2</td><td>20</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void escapedBackslash() {
        assertRendering("Abc|Def\n---|---\n1\\\\|2", "<table>\n" +
                "<thead>\n" +
                "<tr><th>Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td>1\\</td><td>2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void alignLeft() {
        assertRendering("Abc|Def\n:---|---\n1|2", "<table>\n" +
                "<thead>\n" +
                "<tr><th align=\"left\">Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td align=\"left\">1</td><td>2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void alignRight() {
        assertRendering("Abc|Def\n---:|---\n1|2", "<table>\n" +
                "<thead>\n" +
                "<tr><th align=\"right\">Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td align=\"right\">1</td><td>2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void alignCenter() {
        assertRendering("Abc|Def\n:---:|---\n1|2", "<table>\n" +
                "<thead>\n" +
                "<tr><th align=\"center\">Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td align=\"center\">1</td><td>2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void alignCenterSecond() {
        assertRendering("Abc|Def\n---|:---:\n1|2", "<table>\n" +
                "<thead>\n" +
                "<tr><th>Abc</th><th align=\"center\">Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td>1</td><td align=\"center\">2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void alignLeftWithSpaces() {
        assertRendering("Abc|Def\n :--- |---\n1|2", "<table>\n" +
                "<thead>\n" +
                "<tr><th align=\"left\">Abc</th><th>Def</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td align=\"left\">1</td><td>2</td></tr>\n" +
                "</tbody>\n" +
                "</table>\n");
    }

    @Test
    public void alignmentMarkerMustBeNextToDashes() {
        assertRendering("Abc|Def\n: ---|---", "<p>Abc|Def\n: ---|---</p>\n");
        assertRendering("Abc|Def\n--- :|---", "<p>Abc|Def\n--- :|---</p>\n");
        assertRendering("Abc|Def\n---|: ---", "<p>Abc|Def\n---|: ---</p>\n");
        assertRendering("Abc|Def\n---|--- :", "<p>Abc|Def\n---|--- :</p>\n");
    }

    private void assertRendering(String source, String expectedHtml) {
        Parser parser = Parser.builder().customBlockParserFactory(new TableBlockParser.Factory()).build();
        HtmlRenderer renderer = HtmlRenderer.builder().customHtmlRenderer(new TableHtmlRenderer()).build();

        Node node = parser.parse(source);
        String html = renderer.render(node);

        // include source for better assertion errors
        String expected = expectedHtml + "\n\n" + source;
        String actual = html + "\n\n" + source;
        assertEquals(expected, actual);
    }

}
