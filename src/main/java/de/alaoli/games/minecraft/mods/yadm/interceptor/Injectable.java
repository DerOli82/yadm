package de.alaoli.games.minecraft.mods.yadm.interceptor;

import java.util.List;

public interface Injectable
{
	public void injectInto( Object target );
	public void injectInto( List targets );
}
