package com.webAppEmergency.Fire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webAppEmergency.Fire.Fire;
import java.util.List;

@RestController
public class FireRestCrt {

	@Autowired
	FireService fServ;
	
	@RequestMapping(method=RequestMethod.GET, value="/getAll")
	public List<Fire> getAllFire() {
		return fServ.getAllFire();
	} 
	
	@RequestMapping(method=RequestMethod.GET, value="/get/{id}")
	public Fire getFire(@PathVariable int id) {
		return fServ.getFire(id);
	} 
	
//	
//	@RequestMapping(method=RequestMethod.GET,value="/msg/{id1}/{id2}")
//	public String getMsg(@PathVariable String id1, @PathVariable String id2) {
//		String msg1=id1;
//		String msg2=id2;
//		return "Composed Message: msg1:"+msg1+"msg2:"+msg2;
//	}
//	
//	@RequestMapping(method=RequestMethod.GET,value="/parameters")
//	public String getInfoParam(@RequestParam String param1,@RequestParam String param2) {
//		return "Parameters: param1:"+param1+"param2:"+param2;
//	}
}
