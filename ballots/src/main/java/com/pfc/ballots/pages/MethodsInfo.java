package com.pfc.ballots.pages;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library = "context:js/tst.js")
public class MethodsInfo {

	
	@Inject
    private JavaScriptSupport javaScriptSupport;
	
    public void afterRender() {
    JSONArray array=new JSONArray();
    JSONObject ob1=new JSONObject();
    ob1.put("name", "MARIO");
    ob1.put("age", 24);
    array.put(ob1);
    javaScriptSupport.addInitializerCall("tst",ob1);
    
    
    }
    
}
