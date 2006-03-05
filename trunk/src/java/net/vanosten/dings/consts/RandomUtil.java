/*
 * RandomUtil.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2006 Rick Gruber-Riemer (rick@vanosten.net)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.vanosten.dings.consts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.vanosten.dings.model.Entry;

/**
 * Provides convenience methods to work with random sets
 */
public class RandomUtil {
	
	private static Random rand = new Random();
	/**
	 * Private constructor to prevent Initialization
	 */
	private RandomUtil() {
		//nothing to do
	} //END private RandomUtil()
	
	/**
	 * @param number
	 * @return an array of integers of length <code>number</code>, where the 
	 *          array elements are randomly assigned a number between zero (incl.) and 
	 *          <code>number</code> (excl.), such that each element has a unique value.  
	 */
	public static int[] getRandomInts(int number) {
		int[] randInts = new int[number];
		List<Integer> temp = new ArrayList<Integer>(number);
		for (int i = 0; i < number; i++) {
			temp.add(i);
		}
		int pos;
		for (int i = 0; i < number; i++) {
			pos = rand.nextInt(temp.size());
			randInts[i] = temp.get(pos);
			temp.remove(pos);
		}
		return randInts;
	} //END public static int[] getRandomInts(int)
	
	/**
	 * @return a random score based on a random number and the weighted factors for each score
	 */
	public static int getWeightedRandomScore() {
		int foo = rand.nextInt(100);
		int score;
		for (score = Entry.SCORE_MIN; score <= Entry.SCORE_MAX; score++) {
			if (foo < Entry.RANDOM_SCORE_WEIGHT[score-1]) {
				break;
			}
		}
		return score;
	} //END public static int getWeightedRandomScore()
	
	/**
	 * Convenience for not having to initialize Random objects in code
	 * @param maxExclusive
	 * @return The same as java.util.Random.nextInt(int) based on the current
	 *          random instance
	 */
	public static int getRandomPosition(int maxExclusive) {
		return rand.nextInt(maxExclusive);
	} //END public static int getRandomPosition(int)

} //END public class RandomUtil
