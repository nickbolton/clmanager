/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/
package net.deuce.clmanager.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

public class ClManagerPortletController extends AbstractController {

    private String guestUsername;

    public ClManagerPortletController() {
    }

    @Override
    protected void handleActionRequestInternal(ActionRequest req, ActionResponse res) throws Exception {
        super.handleActionRequestInternal(req, res);
    }

    @Override
    protected ModelAndView handleRenderRequestInternal(RenderRequest req, RenderResponse res) throws Exception {
        res.setContentType("text/html");
        String page = req.getParameter("page");
        long pageId = page != null ? Long.parseLong(page) : 1000L;

        String username = req.getRemoteUser();

        return new ModelAndView("navigation");
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    public void setGuestUsername(String guestUsername) {
        this.guestUsername = guestUsername;
    }

}
