package com.mariocairone.log4j2.core.data.finder;

import com.mariocairone.log4j2.api.data.finder.Finder;
import com.mariocairone.log4j2.api.data.finder.Result;
import com.mariocairone.log4j2.api.data.finder.Text;
import com.mariocairone.log4j2.core.data.utils.CharsUtils;

public abstract class AbstractFinder implements Finder {

	@Override
	public Result find(char[] characters) {

		Result result = new Result();
		
		if (characters == null || characters.length == 0)
			return result;

		int[] cachedDelimiters = new int[characters.length];
		int cacheIndex = 0;
		int lastCachedDelimiterIndex = 0;

		for (int startIndex = 0; startIndex < characters.length; startIndex++) {

			char startDelimiter = characters[startIndex];

			if (!isStartDelimiter(startDelimiter))
				continue;

			char endDelimiter = getEndDelimiter(startDelimiter);

			for (int delimiterIndex : cachedDelimiters) {

				char delimiter = characters[delimiterIndex];

				if (delimiterIndex < startIndex || delimiter != endDelimiter)
					continue;

				char[] slice = CharsUtils.slice(characters, startIndex, delimiterIndex);

				if(found(slice)) {	
					result.add(Text.of(slice, startIndex));
					startIndex = delimiterIndex + 1;
					break;
				}

			}

			if (cachedDelimiters.length > 0)
				lastCachedDelimiterIndex = cachedDelimiters[cachedDelimiters.length - 1];

			for (int endIndex = characters.length - 1 - lastCachedDelimiterIndex; endIndex > startIndex; endIndex--) {

				char end = characters[endIndex];

				if (end != endDelimiter)
					continue;

				char[] slice = CharsUtils.slice(characters, startIndex, endIndex);

				if(found(slice)) {	
					result.add(Text.of(slice, startIndex));
					startIndex = endIndex + 1;
					break;
				}
							
				
				cachedDelimiters[cacheIndex] = endIndex;
				cacheIndex++;

			}

		}

		return result;
	}

	protected abstract boolean isStartDelimiter(char character);
	
	protected abstract  boolean found(char[] text);
	
	protected abstract  char getEndDelimiter(char startDelimiter);

	
}
