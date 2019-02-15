package io.nadron.server.netty;

import io.nadron.context.AppContext;
import io.nadron.server.ServerManager;
import net.clanwolf.starmap.logging.C3Logger;

import java.util.HashSet;
import java.util.Set;

public class ServerManagerImpl implements ServerManager
{
	private Set<AbstractNettyServer> servers;
	
	public ServerManagerImpl()
	{
		servers = new HashSet<AbstractNettyServer>();
	}
	
	@Override
	public void startServers(int tcpPort, int flashPort, int udpPort) throws Exception
	{
		
		if(tcpPort > 0)
		{
			AbstractNettyServer tcpServer = (AbstractNettyServer)AppContext.getBean(AppContext.TCP_SERVER);
			tcpServer.startServer(tcpPort);
			servers.add(tcpServer);
		}
		
		if(flashPort > 0)
		{
			AbstractNettyServer flashServer = (AbstractNettyServer)AppContext.getBean(AppContext.FLASH_POLICY_SERVER);
			flashServer.startServer(flashPort);
			servers.add(flashServer);
		}
		
		if(udpPort > 0)
		{
			AbstractNettyServer udpServer = (AbstractNettyServer)AppContext.getBean(AppContext.UDP_SERVER);
			udpServer.startServer(udpPort);
			servers.add(udpServer);
		}
		
	}

	@Override
	public void startServers() throws Exception 
	{
		AbstractNettyServer tcpServer = (AbstractNettyServer)AppContext.getBean(AppContext.TCP_SERVER);
		tcpServer.startServer();
		servers.add(tcpServer);
		AbstractNettyServer flashServer = (AbstractNettyServer)AppContext.getBean(AppContext.FLASH_POLICY_SERVER);
		flashServer.startServer();
		servers.add(flashServer);
		AbstractNettyServer udpServer = (AbstractNettyServer)AppContext.getBean(AppContext.UDP_SERVER);
		udpServer.startServer();
		servers.add(udpServer);
	}
	
	@Override
	public void stopServers() throws Exception
	{
		for(AbstractNettyServer nettyServer: servers){
			try
			{
				nettyServer.stopServer();
			}
			catch (Exception e)
			{
				C3Logger.info("Unable to stop server {} due to error {} " + nettyServer + e);
				throw e;
			}
		}
	}

}
