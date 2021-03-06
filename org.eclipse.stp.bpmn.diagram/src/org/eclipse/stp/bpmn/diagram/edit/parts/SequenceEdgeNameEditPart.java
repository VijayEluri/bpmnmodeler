/*
 * Copyright (c) 2007, Intalio Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Inc. - initial API and implementation
 */
package org.eclipse.stp.bpmn.diagram.edit.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.LabelEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.LabelDirectEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableLabelEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.LabelSnapBackEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.tools.TextDirectEditManager;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.LineBorderEx;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ISemanticParser;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.FillStyle;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.LineStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.stp.bpmn.diagram.edit.policies.BpmnTextSelectionEditPolicy;
import org.eclipse.stp.bpmn.diagram.part.BpmnDiagramPreferenceInitializer;
import org.eclipse.stp.bpmn.diagram.part.BpmnVisualIDRegistry;
import org.eclipse.stp.bpmn.diagram.providers.BpmnElementTypes;
import org.eclipse.stp.bpmn.policies.BpmnDragDropEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;

/**
 * @generated
 */
public class SequenceEdgeNameEditPart extends LabelEditPart implements
        ITextAwareEditPart {

    /**
     * @generated
     */
    public static final int VISUAL_ID = 4012;

    /**
     * @generated
     */
    private DirectEditManager manager;

    /**
     * @generated
     */
    private IParser parser;

    /**
     * @generated
     */
    private List parserElements;

    /**
     * @generated
     */
    private String defaultText;

    /**
     * @generated not
     * changed to have the label nearer to the sequence edge.
     */
    static {
        registerSnapBackPosition(BpmnVisualIDRegistry
                .getType(SequenceEdgeNameEditPart.VISUAL_ID), new Point(0, -10));
    }

    /**
     * @generated
     */
    public SequenceEdgeNameEditPart(View view) {
        super(view);
    }

    /**
     * @generated
     */
    protected void createDefaultEditPoliciesGen() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
                new LabelDirectEditPolicy());
    }

    
    /**
     * @notgenerated
     */
    protected void createDefaultEditPolicies() {
        createDefaultEditPoliciesGen();
     // adding default drag and drop edit policy
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, 
                new BpmnDragDropEditPolicy(this));
        installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,
                new NonResizableLabelEditPolicy());
        // we remove the component edit policy: 
        // we don't want the labels to disappear when pressing Delete.
        removeEditPolicy(EditPolicy.COMPONENT_ROLE);
        installEditPolicy(EditPolicyRoles.SNAP_FEEDBACK_ROLE,
                new LabelSnapBackEditPolicy());
    }
    /**
     * @generated not, when the edge is conditional have the label placed
     * at the source.
     */
    public int getKeyPoint() {
        return ConnectionLocator.MIDDLE;
    }

    /**
     * @generated
     */
    protected String getLabelTextHelper(IFigure figure) {
        if (figure instanceof WrappingLabel) {
            return ((WrappingLabel) figure).getText();
        } else {
            return ((Label) figure).getText();
        }
    }

    /**
     * @generated
     */
    protected void setLabelTextHelper(IFigure figure, String text) {
        if (figure instanceof WrappingLabel) {
            ((WrappingLabel) figure).setText(text);
        } else {
            ((Label) figure).setText(text);
        }
    }

    /**
     * @generated
     */
    protected Image getLabelIconHelper(IFigure figure) {
        if (figure instanceof WrappingLabel) {
            return ((WrappingLabel) figure).getIcon();
        } else {
            return ((Label) figure).getIcon();
        }
    }

    /**
     * @generated
     */
    protected void setLabelIconHelper(IFigure figure, Image icon) {
        if (figure instanceof WrappingLabel) {
            ((WrappingLabel) figure).setIcon(icon);
        } else {
            ((Label) figure).setIcon(icon);
        }
    }

    /**
     * @generated
     */
    public void setLabel(IFigure figure) {
        unregisterVisuals();
        setFigure(figure);
        defaultText = getLabelTextHelper(figure);
        registerVisuals();
        refreshVisuals();
    }

    /**
     * @generated
     */
    protected List getModelChildren() {
        return Collections.EMPTY_LIST;
    }

    /**
     * @generated
     */
    public IGraphicalEditPart getChildBySemanticHint(String semanticHint) {
        return null;
    }

    /**
     * @generated
     */
    protected EObject getParserElement() {
        EObject element = resolveSemanticElement();
        return element != null ? element : (View) getModel();
    }

    /**
     * @generated
     */
    protected Image getLabelIcon() {
        return null;
    }

    /**
     * @generated
     */
    protected String getLabelText() {
        String text = null;
        if (getParser() != null) {
            text = getParser().getPrintString(
                    new EObjectAdapter(getParserElement()),
                    getParserOptions().intValue());
        }
        if (text == null || text.length() == 0) {
            text = defaultText;
        }
        return text;
    }

    /**
     * @generated
     */
    public void setLabelText(String text) {
        setLabelTextHelper(getFigure(), text);
        Object pdEditPolicy = getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
        if (pdEditPolicy instanceof BpmnTextSelectionEditPolicy) {
            ((BpmnTextSelectionEditPolicy) pdEditPolicy).refreshFeedback();
        }
    }

    /**
     * @notgenerated Under linux it seems 
     * to fix issues to have blanks in the text to edit when it is empty.
     */
    public String getEditText() {
        if (TextAnnotationNameEditPart.IS_LINUX) {
            if (getParser() == null) {
                return " "; //$NON-NLS-1$
            }
            String res = getParser().getEditString(
                    new EObjectAdapter(getParserElement()),
                    getParserOptions().intValue());
            if (res != null && res.length() == 0) {
                res = " "; //$NON-NLS-1$
            }
            return res;
        }
        return getEditTextGen();
    }
    
    /**
     * @generated
     */
    public String getEditTextGen() {
        if (getParser() == null) {
            return ""; //$NON-NLS-1$
        }
        return getParser().getEditString(
                new EObjectAdapter(getParserElement()),
                getParserOptions().intValue());
    }

    /**
     * @generated
     */
    protected boolean isEditable() {
        return getEditText() != null;
    }

    /**
     * @generated
     */
    public ICellEditorValidator getEditTextValidator() {
        return new ICellEditorValidator() {

            public String isValid(final Object value) {
                if (value instanceof String) {
                    final EObject element = getParserElement();
                    final IParser parser = getParser();
                    try {
                        IParserEditStatus valid = (IParserEditStatus) getEditingDomain()
                                .runExclusive(new RunnableWithResult.Impl() {

                                    public void run() {
                                        setResult(parser.isValidEditString(
                                                new EObjectAdapter(element),
                                                (String) value));
                                    }
                                });
                        return valid.getCode() == ParserEditStatus.EDITABLE ? null
                                : valid.getMessage();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }

                // shouldn't get here
                return null;
            }
        };
    }

    /**
     * @generated
     */
    public IContentAssistProcessor getCompletionProcessor() {
        if (getParser() == null) {
            return null;
        }
        return getParser().getCompletionProcessor(
                new EObjectAdapter(getParserElement()));
    }

    /**
     * @generated
     */
    public ParserOptions getParserOptions() {
        return ParserOptions.NONE;
    }

    /**
     * @generated
     */
    public IParser getParser() {
        if (parser == null) {
            String parserHint = ((View) getModel()).getType();
            ParserHintAdapter hintAdapter = new ParserHintAdapter(
                    getParserElement(), parserHint) {

                public Object getAdapter(Class adapter) {
                    if (IElementType.class.equals(adapter)) {
                        return BpmnElementTypes.SequenceEdge_3001;
                    }
                    return super.getAdapter(adapter);
                }
            };
            parser = ParserService.getInstance().getParser(hintAdapter);
        }
        return parser;
    }

    /**
     * @generated NOT: override the text cell editor class to support enter+shift and enter+alt to enter newlines.
     */
    protected DirectEditManager getManager() {
        if (manager == null) {
            setManager(new TextDirectEditManager(this, BpmnEditPartFactory
                    .getTextCellEditorClass(this), BpmnEditPartFactory
                    .getTextCellEditorLocator(this)));
        }
        return manager;
    }


    /**
     * @generated
     */
    protected void setManager(DirectEditManager manager) {
        this.manager = manager;
    }

    /**
     * @generated
     */
    protected void performDirectEdit() {
        getManager().show();
    }

    /**
     * @generated
     */
    protected void performDirectEdit(Point eventLocation) {
        if (getManager().getClass() == TextDirectEditManager.class) {
            ((TextDirectEditManager) getManager()).show(eventLocation
                    .getSWTPoint());
        }
    }

    /**
     * @generated
     */
    private void performDirectEdit(char initialCharacter) {
        if (getManager() instanceof TextDirectEditManager) {
            ((TextDirectEditManager) getManager()).show(initialCharacter);
        } else {
            performDirectEdit();
        }
    }

    /**
     * @generated
     */
    protected void performDirectEditRequest(Request request) {
        final Request theRequest = request;
        try {
            getEditingDomain().runExclusive(new Runnable() {

                public void run() {
                    if (isActive() && isEditable()) {
                        if (theRequest
                                .getExtendedData()
                                .get(
                                        RequestConstants.REQ_DIRECTEDIT_EXTENDEDDATA_INITIAL_CHAR) instanceof Character) {
                            Character initialChar = (Character) theRequest
                                    .getExtendedData()
                                    .get(
                                            RequestConstants.REQ_DIRECTEDIT_EXTENDEDDATA_INITIAL_CHAR);
                            performDirectEdit(initialChar.charValue());
                        } else if ((theRequest instanceof DirectEditRequest)
                                && (getEditText().equals(getLabelText()))) {
                            DirectEditRequest editRequest = (DirectEditRequest) theRequest;
                            performDirectEdit(editRequest.getLocation());
                        } else {
                            performDirectEdit();
                        }
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @generated NOT added refreshing for line and background style.
     */
    protected void refreshVisuals() {
        super.refreshVisuals();
        refreshForegroundColor();
        refreshBackgroundColor();
        refreshLabel();
        refreshFont();
        refreshFontColor();
        refreshUnderline();
        refreshStrikeThrough();
    }
    
    
    private boolean needBorder() {
        return getLabelIcon() != null 
            || (getLabelText() != null && !getLabelText().trim().equals("")); //$NON-NLS-1$
    }
    
    /** Refresh the border's figure foreground colour. */
    protected void refreshForegroundColor() {
        LineStyle style = (LineStyle)  ((View) getModel()).getStyle(NotationPackage.Literals.LINE_STYLE);
        if ( style != null ) {
            if (style.getLineColor() == FigureUtilities.RGBToInteger(
                    BpmnDiagramPreferenceInitializer.TRANSPARENCY_COLOR) 
                    || !needBorder()) {
                if (getFigure().getBorder() != null) {
                    getFigure().setBorder(null);
                }
            } else {
                if (getFigure().getBorder() == null) {
                    getFigure().setBorder(new LineBorderEx(1));
                }
                Border border = getFigure().getBorder();
                if (border instanceof LineBorder) {
                    ((LineBorder) border).setColor(DiagramColorRegistry.getInstance().
                            getColor(new Integer(style.getLineColor())));
                }
            }
        }
    }
    
    /** Refresh the figure background colour. */
    protected void refreshBackgroundColor() {
        FillStyle style = (FillStyle)  ((View) getModel()).getStyle(NotationPackage.Literals.FILL_STYLE);
        if ( style != null ) {
            if (style.getFillColor() == FigureUtilities.RGBToInteger(
                    BpmnDiagramPreferenceInitializer.TRANSPARENCY_COLOR) 
                    || !needBorder()) {
                getFigure().setBackgroundColor(null);
                getFigure().setOpaque(false);
            } else {
                getFigure().setBackgroundColor(DiagramColorRegistry.getInstance().
                        getColor(new Integer(style.getFillColor())));
                getFigure().setOpaque(true);
            }
        }
    }
    
    /**
     * @generated
     */
    protected void refreshLabel() {
        setLabelTextHelper(getFigure(), getLabelText());
        setLabelIconHelper(getFigure(), getLabelIcon());
        Object pdEditPolicy = getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
        if (pdEditPolicy instanceof BpmnTextSelectionEditPolicy) {
            ((BpmnTextSelectionEditPolicy) pdEditPolicy).refreshFeedback();
        }
    }

    
    /**
     * @generated
     */
    protected void refreshUnderline() {
        FontStyle style = (FontStyle) getFontStyleOwnerView().getStyle(
                NotationPackage.eINSTANCE.getFontStyle());
        if (style != null && getFigure() instanceof WrappingLabel) {
            ((WrappingLabel) getFigure()).setTextUnderline(style.isUnderline());
        }
    }

    /**
     * @generated
     */
    protected void refreshStrikeThrough() {
        FontStyle style = (FontStyle) getFontStyleOwnerView().getStyle(
                NotationPackage.eINSTANCE.getFontStyle());
        if (style != null && getFigure() instanceof WrappingLabel) {
            ((WrappingLabel) getFigure()).setTextStrikeThrough(style
                    .isStrikeThrough());
        }
    }

    /**
     * @generated
     */
    protected void refreshFont() {
        FontStyle style = (FontStyle) getFontStyleOwnerView().getStyle(
                NotationPackage.eINSTANCE.getFontStyle());
        if (style != null) {
            FontData fontData = new FontData(style.getFontName(), style
                    .getFontHeight(), (style.isBold() ? SWT.BOLD : SWT.NORMAL)
                    | (style.isItalic() ? SWT.ITALIC : SWT.NORMAL));
            setFont(fontData);
        }
    }

    /**
     * @generated
     */
    protected void setFontColor(Color color) {
        getFigure().setForegroundColor(color);
    }

    /**
     * @generated
     */
    protected void addSemanticListeners() {
        if (getParser() instanceof ISemanticParser) {
            EObject element = resolveSemanticElement();
            parserElements = ((ISemanticParser) getParser())
                    .getSemanticElementsBeingParsed(element);
            for (int i = 0; i < parserElements.size(); i++) {
                addListenerFilter(
                        "SemanticModel" + i, this, (EObject) parserElements.get(i)); //$NON-NLS-1$
            }
        } else {
            super.addSemanticListeners();
        }
    }

    /**
     * @generated
     */
    protected void removeSemanticListeners() {
        if (parserElements != null) {
            for (int i = 0; i < parserElements.size(); i++) {
                removeListenerFilter("SemanticModel" + i); //$NON-NLS-1$
            }
        } else {
            super.removeSemanticListeners();
        }
    }

    /**
     * @generated
     */
    protected AccessibleEditPart getAccessibleEditPart() {
        if (accessibleEP == null) {
            accessibleEP = new AccessibleGraphicalEditPart() {

                public void getName(AccessibleEvent e) {
                    e.result = getLabelTextHelper(getFigure());
                }
            };
        }
        return accessibleEP;
    }

    /**
     * @generated
     */
    private View getFontStyleOwnerView() {
        return getPrimaryView();
    }

    /**
     * @generated
     */
    protected void handleNotificationEvent(Notification event) {
        Object feature = event.getFeature();
        if (NotationPackage.eINSTANCE.getFontStyle_FontColor().equals(feature)) {
            Integer c = (Integer) event.getNewValue();
            setFontColor(DiagramColorRegistry.getInstance().getColor(c));
        } else if (NotationPackage.eINSTANCE.getFontStyle_Underline().equals(
                feature)) {
            refreshUnderline();
        } else if (NotationPackage.eINSTANCE.getFontStyle_StrikeThrough()
                .equals(feature)) {
            refreshStrikeThrough();
        } else if (NotationPackage.eINSTANCE.getFontStyle_FontHeight().equals(
                feature)
                || NotationPackage.eINSTANCE.getFontStyle_FontName().equals(
                        feature)
                || NotationPackage.eINSTANCE.getFontStyle_Bold()
                        .equals(feature)
                || NotationPackage.eINSTANCE.getFontStyle_Italic().equals(
                        feature)) {
            refreshFont();
        } else if (NotationPackage.eINSTANCE.getLineStyle_LineColor().equals(feature) ||
                NotationPackage.eINSTANCE.getFillStyle_FillColor().equals(feature)) {
            refreshVisuals();
            return; // forbid going in the super method
        } else {
            if (getParser() != null
                    && getParser().isAffectingEvent(event,
                            getParserOptions().intValue())) {
                refreshForegroundColor();
                refreshBackgroundColor();
                refreshLabel();
            }
            if (getParser() instanceof ISemanticParser) {
                ISemanticParser modelParser = (ISemanticParser) getParser();
                if (modelParser.areSemanticElementsAffected(null, event)) {
                    removeSemanticListeners();
                    if (resolveSemanticElement() != null) {
                        addSemanticListeners();
                    }
                    refreshLabel();
                }
            }
        } 
        super.handleNotificationEvent(event);
    }

    /**
     * @generated
     */
    protected IFigure createFigure() {
        IFigure label = createFigurePrim();
        defaultText = getLabelTextHelper(label);
        return label;
    }

    /**
     * @generated
     */
    protected IFigure createFigurePrim() {
        return new EdgeNameFigure();
    }

    /**
     * @generated NOT wrappingLabel joys.
     */
    public class EdgeNameFigure extends WrappingLabel {

        /**
         * @generated NOT set textWrap true
         */
        public EdgeNameFigure() {

            this.setText(""); //$NON-NLS-1$
            setTextWrap(true);
            createContents();
        }

        /**
         * @generated 
         */
        private void createContents() {
        	
        }

        /**
         * @generated
         */
        private boolean myUseLocalCoordinates = false;

        /**
         * @generated
         */
        protected boolean useLocalCoordinates() {
            return myUseLocalCoordinates;
        }

        /**
         * @generated
         */
        protected void setUseLocalCoordinates(boolean useLocalCoordinates) {
            myUseLocalCoordinates = useLocalCoordinates;
        }

    }

    /**
     * @generated NOT we override default settings to have our own border.
     */
    @Override
    public Object getPreferredValue(EStructuralFeature feature) {
        if (NotationPackage.eINSTANCE.getLineStyle_LineColor().equals(feature) ||
                NotationPackage.eINSTANCE.getFillStyle_FillColor().equals(feature)) {
            return FigureUtilities.RGBToInteger(BpmnDiagramPreferenceInitializer.TRANSPARENCY_COLOR); 
            // we customize the value returned by default
            // for the label border. This value triggers border transparency
        }
        return super.getPreferredValue(feature);
    }
}
