/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MDBTopicCaller", urlPatterns = {"/MDBTopicCaller"})
public class MDBTopicCaller extends HttpServlet {

    @Resource(mappedName = "MyTopic")
    private Topic myTopic;

    @Inject
    @JMSConnectionFactory("jms/TopicConFactory")
    private JMSContext context;

     @Resource(mappedName = "jms/TopicConFactory")
    private ConnectionFactory connectionFactory;


    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String clientID = request.getParameter("un");
        String msg = request.getParameter("msg");
        
        
        sendJMSMessageToMyTopic(clientID, msg);
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void sendJMSMessageToMyTopic(String clientID, String messageData) {
        
        try {
            
            Connection c = connectionFactory.createConnection();
            Session s = c.createSession();
            MessageProducer messageProducer = s.createProducer(myTopic);
            
            TextMessage tm = s.createTextMessage(clientID + " - " + messageData);
            messageProducer.send(tm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //context.createProducer().send(myTopic, messageData);
    }



}
