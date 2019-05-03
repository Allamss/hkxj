package cn.hkxj.platform.controller.wechat;

import cn.hkxj.platform.config.wechat.WechatMpConfiguration;
import cn.hkxj.platform.service.wechat.HandlerRouteService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@RestController
@RequestMapping("/wechat/portal/{appid}")
public class WechatController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

//	private final WxMpService wxService;

//	private final WxMpMessageRouter router;

//	private final HandlerRouteService handlerService;
//
//	@Autowired
//	public WechatController(HandlerRouteService handlerService) {
//		this.handlerService = handlerService;
//		init();
//	}

//	@Autowired
//	public WechatController(WxMpService wxService, WxMpMessageRouter router, HandlerRouteService handlerService) {
//		this.wxService = wxService;
//		this.router = router;
//		this.handlerService = handlerService;
//		init();
//	}


//	private void init() {
//		this.handlerService.handlerRegister();
//	}

	@GetMapping(produces = "text/plain;charset=utf-8")
	public String authGet(@PathVariable String appid,
			@RequestParam(name = "signature", required = false) String signature,
			@RequestParam(name = "timestamp", required = false) String timestamp,
			@RequestParam(name = "nonce", required = false) String nonce,
			@RequestParam(name = "echostr", required = false) String echostr) {

		this.logger.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
				timestamp, nonce, echostr);

		if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
			return ("请求参数非法，请核实!");
		}
		final WxMpService wxMpService = WechatMpConfiguration.getMpServices().get(appid);
		if (wxMpService.checkSignature(timestamp, nonce, signature)) {
			return echostr;
		}

		return "非法请求";
	}

	@PostMapping(produces = "application/xml; charset=UTF-8")
	public String post(@PathVariable String appid,
					   @RequestBody String requestBody,
	                   @RequestParam("signature") String signature,
	                   @RequestParam("timestamp") String timestamp,
	                   @RequestParam("nonce") String nonce,
	                   @RequestParam(name = "encrypt_type",
			                   required = false) String encType,
	                   @RequestParam(name = "msg_signature",
			                   required = false) String msgSignature) {
		log.info(
				"\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
						+ " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
				signature, encType, msgSignature, timestamp, nonce, requestBody);
		final WxMpService wxService = WechatMpConfiguration.getMpServices().get(appid);
		if (!wxService.checkSignature(timestamp, nonce, signature)) {
			throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
		}

		String out = null;
		if (encType == null) {
			// 明文传输的消息

			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            MDC.put("openid", inMessage.getFromUser());
			WxMpXmlOutMessage outMessage = this.route(inMessage, appid);
			if (outMessage == null) {
				return "success";
			}

			out = outMessage.toXml();
		} else if ("aes".equals(encType)) {
			// aes加密的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
					requestBody, wxService.getWxMpConfigStorage(), timestamp,
					nonce, msgSignature);
			this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
			WxMpXmlOutMessage outMessage = this.route(inMessage, appid);
			if (outMessage == null) {
				return "";
			}

			out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
		}
        log.info("wechat message out {}", out);

		return out;
	}

	private WxMpXmlOutMessage route(WxMpXmlMessage message, String appid) {
		try {
			return WechatMpConfiguration.getRouters().get(appid).route(message);
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
		}

		return null;
	}


}
