package ${groupId}.service.game;

import javax.servlet.http.HttpServletRequest;

import io.netty.channel.ChannelHandlerContext;
import ${groupId}.domain.game.GameRequest;
import ${groupId}.domain.result.Result;

public interface GameService {
	
	public Result login(String code, String deviceType, HttpServletRequest request);
	
	public Result login1(String code, String deviceType, HttpServletRequest request);
	
	public Result getIpByRoomId(String token, Long roomId);
	
	public Result entryHall(ChannelHandlerContext ctx, GameRequest request);
	
	public Result createRoom(ChannelHandlerContext ctx, GameRequest request);
	
	public Result entryRoom(ChannelHandlerContext ctx, GameRequest request);
	
	public Result ready(ChannelHandlerContext ctx, GameRequest request);
	
	public Result dealCards(ChannelHandlerContext ctx, GameRequest request);
	
	public Result stake(ChannelHandlerContext ctx, GameRequest request);
	
	public Result watchCards(ChannelHandlerContext ctx, GameRequest request);
	
	public Result manualCardsCompare(ChannelHandlerContext ctx, GameRequest request);
	
	public Result discardCards(ChannelHandlerContext ctx, GameRequest request);
	
	public Result curSettlement(ChannelHandlerContext ctx, GameRequest request);
	
	public Result chatMsg(ChannelHandlerContext ctx, GameRequest request);
	
	public Result totalSettlement(ChannelHandlerContext ctx, GameRequest request);
	
	public Result dissolveRoom(ChannelHandlerContext ctx, GameRequest request);
	
	public Result agreeDissolveRoom(ChannelHandlerContext ctx, GameRequest request);
	
	public Result disagreeDissolveRoom(ChannelHandlerContext ctx, GameRequest request);
	
	public Result refreshRoom(ChannelHandlerContext ctx, GameRequest request);
	
	public Result delRoomConfirmBeforeReturnHall(ChannelHandlerContext ctx, GameRequest request);
	
	public Result queryOtherPlayerInfo(ChannelHandlerContext ctx, GameRequest request);
	
	public Result userRecord(ChannelHandlerContext ctx, GameRequest request);
	
	public Result userFeedback(ChannelHandlerContext ctx, GameRequest request);
	
	public Result updatePlayerInfo(ChannelHandlerContext ctx, GameRequest request);
	
	public Result notice(ChannelHandlerContext ctx, GameRequest request);
	
	public Result sendEmoticon(ChannelHandlerContext ctx, GameRequest request);
	
}
