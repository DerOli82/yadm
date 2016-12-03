package de.alaoli.games.minecraft.mods.yadm.comparator;

import java.util.Comparator;

import de.alaoli.games.minecraft.mods.yadm.event.PerformWorldBorderEvent;

public class PriorityComparator implements Comparator<PerformWorldBorderEvent> 
{
	/********************************************************************************
	 * Methods - Implement WorldBorderAction
	 ********************************************************************************/
	
	@Override
	public int compare( PerformWorldBorderEvent priorityA, PerformWorldBorderEvent priorityB )
	{
		if( priorityA.priority() == priorityB.priority() )
		{
			return 0;
		}
		else if( priorityA.priority() > priorityB.priority() )
		{
			return -1;
		}
		else if( priorityA.priority() < priorityB.priority() )
		{
			return 1;
		}
		else
		{
			//Else shouldn't happen
			return 0;
		}
	}

}
