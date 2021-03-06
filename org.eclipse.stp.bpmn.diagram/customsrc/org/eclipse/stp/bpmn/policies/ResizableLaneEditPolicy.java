/******************************************************************************
 * Copyright (c) 2006, Intalio Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Inc. - initial API and implementation
 *******************************************************************************/

/** 
 * Date           	Author          Changes 
 * 04 dec 2006   	hmalphettes  	Created 
 **/

package org.eclipse.stp.bpmn.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.LineStyle;
import org.eclipse.stp.bpmn.Activity;
import org.eclipse.stp.bpmn.Lane;
import org.eclipse.stp.bpmn.diagram.BpmnDiagramMessages;
import org.eclipse.stp.bpmn.diagram.edit.parts.Activity2EditPart;
import org.eclipse.stp.bpmn.diagram.edit.parts.ActivityEditPart;
import org.eclipse.stp.bpmn.diagram.edit.parts.LaneEditPart;
import org.eclipse.stp.bpmn.diagram.edit.parts.PoolPoolCompartmentEditPart;
import org.eclipse.stp.bpmn.diagram.edit.parts.SubProcessEditPart;
import org.eclipse.swt.SWT;

/**
 * Resize edit policy for lanes: only the bottom lane and if it is not
 * the lane at the bottom.
 * 
 * @author hmalphettes
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 */
public class ResizableLaneEditPolicy extends ResizableShapeEditPolicyEx {
    
    /**
     * This command sets the activities on the lane.
     *
     * @author <a href="http://www.intalio.com">Intalio Inc.</a>
     * @author <a href="mailto:atoulme@intalio.com">Antoine Toulme</a>
     */
    public static class SetActivitiesCommand extends AbstractTransactionalCommand {

        private Lane _lane;
        
        private List<Activity> _activities;

        private CreateViewAndElementRequest _request;
        
        public SetActivitiesCommand(Lane elt, List<Activity> activities) {
            super((TransactionalEditingDomain) AdapterFactoryEditingDomain.
                    getEditingDomainFor(elt),
                    BpmnDiagramMessages.ResizableGroupEditPolicy_command_name, 
                    getWorkspaceFiles(elt));
            _lane = elt;
            _activities = activities;
        }

        /**
         * Constructor for newly created lane.
         * 
         * @param request the request which will contain the new activity
         * @param container the container needed to resolve the editing domain
         */
        public SetActivitiesCommand(List<Activity> activities, 
                CreateViewAndElementRequest request, 
                EObject container) {
            super((TransactionalEditingDomain) AdapterFactoryEditingDomain.
                    getEditingDomainFor(container),
                    BpmnDiagramMessages.ResizableActivityEditPolicy_groups_command_name, 
                    getWorkspaceFiles(container));
            _request = request;
            _activities = activities;
        }
        @Override
        protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
                IAdaptable info) throws ExecutionException {
            if (_request != null) {
                _lane = (Lane) _request.getViewAndElementDescriptor().
                    getCreateElementRequestAdapter().resolve();
            }
            List<Activity> toRemove = new ArrayList<Activity>();
            List<Activity> activities = new ArrayList<Activity>(_activities);
            for (Activity a : _lane.getActivities()) {
                if (!activities.remove(a)) {
                    toRemove.add(a);
                }
            }
            
            _lane.getActivities().removeAll(toRemove);
            _lane.getActivities().addAll(activities);
            return CommandResult.newOKCommandResult();
        }
        
    }
    
    public static List<Activity> findContainedActivities(ChangeBoundsRequest request, LaneEditPart laneEditPart) {
        Rectangle rect = laneEditPart.getFigure().getBounds().getCopy();
        laneEditPart.getFigure().translateToAbsolute(rect);
        rect.translate(request.getMoveDelta());
        rect.resize(request.getSizeDelta());
        return findContainedActivities(rect, laneEditPart);
    }
    
    public static List<Activity> findContainedActivities(Rectangle rect, LaneEditPart laneEditPart) {
        return findContainedActivities(rect, laneEditPart.getViewer());
    }
    
    public static List<Activity> findContainedActivities(Rectangle rect,
            EditPartViewer viewer) {
        List<Activity> activities = new ArrayList<Activity>();
        for (Object fig : viewer.getVisualPartMap().keySet()) {
            Object part = viewer.getVisualPartMap().get(fig);
            if (part instanceof ActivityEditPart ||
                    part instanceof Activity2EditPart ||
                    part instanceof SubProcessEditPart) {
                Rectangle bounds = ((IGraphicalEditPart) part).getFigure().getBounds().getCopy();
                ((IGraphicalEditPart) part).getFigure().translateToAbsolute(bounds);
                if ((bounds.y > rect.y 
                        && bounds.y < rect.y + rect.height) 
                    || (bounds.y + bounds.height > rect.y 
                            && bounds.y + bounds.height < rect.y + rect.height)) {
                    activities.add((Activity) ((IGraphicalEditPart) part).resolveSemanticElement());
                }
            }
        }
        return activities;
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.stp.bpmn.policies.ResizableShapeEditPolicyEx#createHandle(org.eclipse.gef.GraphicalEditPart,
     *      int)
     */
    protected Handle createHandle(GraphicalEditPart owner, int direction) {
        return new LaneResizeHandle(owner, direction);
    }

    /**
     * Resize handle class.
     */
    protected static class LaneResizeHandle extends ResizeHandleEx {
        /**
         * Creates new instance of handle
         * 
         * @param owner
         *            the owner
         * @param direction
         *            handle direction
         */
        public LaneResizeHandle(GraphicalEditPart owner, int direction) {
            super(owner, direction);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.stp.bpmn.policies.ResizableShapeEditPolicyEx.ResizeHandleEx#createDragTracker()
         */
        @Override
        protected DragTracker createDragTracker() {
            return new ResizeTracker(getOwner(), cursorDirection);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#getMoveCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
     */
    @Override
    protected Command getMoveCommand(ChangeBoundsRequest request) {
        ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_MOVE_CHILDREN);
        req.setEditParts(request.getEditParts());
        req.setMoveDelta(new Point(0,request.getMoveDelta().y));
        req.setSizeDelta(new Dimension(0,request.getSizeDelta().height));
        req.setLocation(request.getLocation());
        req.setExtendedData(request.getExtendedData());
        req.setResizeDirection(PositionConstants.NORTH_SOUTH);
        List<Activity> activities = findContainedActivities(request, (LaneEditPart) getHost());
        CompoundCommand compound = new CompoundCommand();
        SetActivitiesCommand command = new SetActivitiesCommand(
                (Lane) ((IGraphicalEditPart) getHost()).resolveSemanticElement(), 
                activities);
        compound.add(new ICommandProxy(command));
        compound.add(getHost().getParent().getCommand(req));
        return compound;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.ResizableEditPolicy#getResizeCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
     */
    @Override
    protected Command getResizeCommand(ChangeBoundsRequest request) {
        // if you want a lane to be expanded to the north and the lane happens to be the first lane,
        // and there is no space above it, forbid the resize.
        if (request.getMoveDelta().y < 0 && 
                getHostFigure().getBounds().y == PoolPoolCompartmentEditPart.INSETS.top) {
            return UnexecutableCommand.INSTANCE;
        }
        
        ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_RESIZE_CHILDREN);
        req.setEditParts(request.getEditParts());

        req.setMoveDelta(request.getMoveDelta());
        req.setSizeDelta(request.getSizeDelta());
        req.setLocation(request.getLocation());
        req.setExtendedData(request.getExtendedData());
        req.setResizeDirection(request.getResizeDirection());
        CompoundCommand compound = new CompoundCommand();
        compound.add(getHost().getParent().getCommand(req));
        return compound;
    }

    protected void replaceHandleDragEditPartsTracker(Handle handle) {
        if (handle instanceof AbstractHandle) {
            AbstractHandle h = (AbstractHandle) handle;
            h.setDragTracker(new DragEditPartsTrackerEx(getHost()) {
                @Override
                protected void addSourceCommands(boolean isMove,
                        CompoundCommand command) {

                    if (!isCloneActive()) {
                        Request request = getTargetRequest();
                        request.setType(isMove ? REQ_MOVE
                                : RequestConstants.REQ_DRAG);
                        command.add(getHost().getCommand(request));
                        // Iterator iter = getOperationSet().iterator();
                        // while (iter.hasNext()) {
                        // EditPart editPart = (EditPart) iter.next();
                        // command.add(editPart.getCommand(request));
                        // }
                        request.setType(RequestConstants.REQ_DROP);
                    } else {
                        super.addSourceCommands(isMove, command);
                    }
                }
            });
        }
    }
    
    /**
     * Only allow for creating handles on north and south of the shape.
     */
    @Override
    protected List createSelectionHandles() {
        List<Handle> list = new ArrayList<Handle>();
        ResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(),
                list);
        ((MoveHandle) list.get(0)).setBorder(new LaneResizeHandleBorder());
        GraphicalEditPart part = (GraphicalEditPart) getHost();
        list.add(createHandle(part, PositionConstants.SOUTH));
        list.add(createHandle(part, PositionConstants.NORTH));

        return list;
    }
    
    /**
     * A lean resize handle border to show it is possible to resize on south and north only.
     * @author Antoine Toulme
     *
     */
    private class LaneResizeHandleBorder extends LineBorder {
        
        /**
         * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
         */
        public void paint(IFigure figure, Graphics graphics, Insets insets) {
                tempRect.setBounds(getPaintRectangle(figure, new Insets(1, 0, 1, 0)));
                if (getWidth() % 2 == 1) {
                        tempRect.width--;
                        tempRect.height--;
                }
                tempRect.shrink(getWidth() / 2, getWidth() / 2);
                
                graphics.drawLine(tempRect.getBottomLeft(), tempRect.getBottomRight());
                graphics.drawLine(tempRect.getTopLeft(), tempRect.getTopRight());
        }
    }

}
