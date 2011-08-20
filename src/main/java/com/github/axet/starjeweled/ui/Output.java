package com.github.axet.starjeweled.ui;

import java.net.URI;

import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class Output implements HyperlinkListener {
    JTextPane pane;
    HTMLEditorKit kit = new HTMLEditorKit();
    HTMLDocument doc = new HTMLDocument();

    public Output(JTextPane pane) {
        pane.setEditorKit(kit);
        pane.setDocument(doc);

        pane.addHyperlinkListener(this);

        doc.getStyleSheet().addRule("A {color:blue;text-decoration:underline}");

    }

    public void begin(String html) {
        try {
            Element[] roots = doc.getRootElements();
            Element body = null;
            for (int i = 0; i < roots[0].getElementCount(); i++) {
                Element element = roots[0].getElement(i);
                if (element.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY) {
                    body = element;
                    break;
                }
            }
            kit.insertHTML(doc, body.getStartOffset(), "<pre>" + html + "</pre>", 0, 0, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void end(String html) {
        try {
            kit.insertHTML(doc, doc.getLength(), html, 0, 0, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void hyperlinkUpdate(HyperlinkEvent arg0) {
        try {

            if (arg0.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                java.awt.Desktop.getDesktop().browse(new URI(arg0.getURL().toString()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}