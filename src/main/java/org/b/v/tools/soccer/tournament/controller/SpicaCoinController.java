package org.b.v.tools.soccer.tournament.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SpicaCoinController {
	
	private static Logger LOG = Logger.getLogger(SpicaCoinController.class);
	
//	@Autowired
//	private GreetingRepository repo;
//	
//	@Autowired
//	private BalanceRepository balanceRepo;
	
	public SpicaCoinController() {
	}
	
	@RequestMapping("/tournament/{name}")
	public String createTournament(@PathVariable("name") String name) {
		LOG.debug(name);
		return "aha";
	}
	
//	@RequestMapping(value="/greeting",method = RequestMethod.GET)
//	public Greeting greeting() {
//		System.out.println("received greeting");
//		return new Greeting("1","test");
//	}
//	
//	@RequestMapping(value="/balance/{uuid}",method = RequestMethod.GET)
//	public ExchangeReply getBalance(@PathVariable("uuid") String uuid) {
//		System.out.println(uuid);
//		Balance balance = balanceRepo.findOne(uuid);
//		return new ExchangeReply(true).withSpicoinBalance(balance!=null?balance.getBalance():0);
//	}
//	
//	@RequestMapping(value="/balance/{uuid}/exchange",method = RequestMethod.POST)
//	public ExchangeReply exchange(@PathVariable("uuid") String uuid,@RequestBody Exchange change) {
//		System.out.println("received exchange for " + uuid);
//
//		Balance balance = balanceRepo.findOne(uuid);
//		if(balance == null) {
//			return new ExchangeReply(false).withSpicoinBalance(0);
//		}
//		
//		try {
//			balance.pay(change.getAmount());
//			balanceRepo.save(balance);
//		} catch (CannotPayException e) {
//			return new ExchangeReply(false).withSpicoinBalance(balance.getBalance());
//		}
//		
//		return new ExchangeReply(true);
//	}
//	
//	@RequestMapping(value="/balance/{uuid}/charge",method = RequestMethod.POST)
//	public ExchangeReply charge(@PathVariable("uuid") String uuid,@RequestBody Exchange change) {
//		System.out.println("received charge");
//		Balance balance = balanceRepo.findOne(change.getUid());
//		if(balance == null) {
//			balance = new Balance(uuid);
//		}
//		balance.receive(change.getAmount());
//		balanceRepo.save(balance);
//		return new ExchangeReply(true);
//	}
	
}
