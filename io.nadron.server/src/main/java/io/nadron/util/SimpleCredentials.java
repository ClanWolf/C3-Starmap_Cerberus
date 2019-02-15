package io.nadron.util;

import io.netty.buffer.ByteBuf;

public class SimpleCredentials implements Credentials
{
	private final String username;
	private final String password;
	
	public SimpleCredentials(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
	public SimpleCredentials(ByteBuf buffer)
	{
		this.username = NettyUtils.readString(buffer);
		this.password = NettyUtils.readString(buffer);
	}

	@Override
	public String getUsername()
	{
		return username;
	}

	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public String toString()
	{
		return username;
	}
}
