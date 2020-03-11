package com.mariocairone.log4j2.core.data.masker;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.amariocairone.log4j2.api.data.exceptions.InvalidInputException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.mariocairone.log4j2.api.data.finder.Result;
import com.mariocairone.log4j2.api.data.finder.Text;
import com.mariocairone.log4j2.api.data.masker.Masker;
import com.mariocairone.log4j2.api.data.masker.MaskingEngine;
import com.mariocairone.log4j2.core.data.finder.JsonFinder;
import com.mariocairone.log4j2.core.data.masker.BasicMaskingEngine;
import com.mariocairone.log4j2.core.data.utils.CharsUtils;

public class JsonMasker implements Masker {


	private static final Configuration jsonPathConfig = Configuration.builder()
			.jsonProvider(new JacksonJsonNodeJsonProvider()).options(Option.AS_PATH_LIST, Option.SUPPRESS_EXCEPTIONS)
			.build();
	private static final ObjectMapper mapper = new ObjectMapper();

	private final Set<String> whitelistedKeys;
	private final Set<JsonPath> whitelistedJsonPaths;
	private final boolean enabled;
	
	private JsonFinder jsonFinder;
	
	private MaskingEngine maskingEngine;

	public JsonMasker(Collection<String> whitelist, boolean enabled, MaskingEngine maskingEngine) {
		this.enabled = enabled;
		this.jsonFinder = new JsonFinder();
		this.maskingEngine = maskingEngine;
		
		whitelistedKeys = new HashSet<>();
		whitelistedJsonPaths = new HashSet<>();

		whitelist.forEach(item -> {
			if (item.startsWith("$")) {
				whitelistedJsonPaths.add(JsonPath.compile(item));
			} else {
				whitelistedKeys.add(item.toUpperCase());
			}
		});
		
		
	}	
	
	public JsonMasker() {
		this(Collections.emptySet(), true);
	}

	public JsonMasker(boolean enabled) {
		this(Collections.emptySet(), enabled);
	}

	public JsonMasker(Collection<String> whitelist) {
		this(whitelist, true);
	}
	
	public JsonMasker(Collection<String> whitelist, boolean enabled) {
		this(whitelist, enabled, new BasicMaskingEngine());
	}
	
	private JsonNode mask(JsonNode target) {
		if (!enabled)
			return target;
		if (target == null)
			 throw  new InvalidInputException();

		Set<String> expandedWhitelistedPaths = new HashSet<>();
		for (JsonPath jsonPath : whitelistedJsonPaths) {
			if (jsonPath.isDefinite()) {
				expandedWhitelistedPaths.add(jsonPath.getPath());
			} else {
				for (JsonNode node : jsonPath.<ArrayNode>read(target, jsonPathConfig)) {
					expandedWhitelistedPaths.add(node.asText());
				}
			}
		}

		return traverseAndMask(target.deepCopy(), expandedWhitelistedPaths, "$");
	}

	private JsonNode traverseAndMask(JsonNode target, Set<String> expandedWhitelistedPaths, String path) {
		if (target.isTextual()) {
			return new TextNode(maskingEngine.maskString(target.asText()));
		}
		if (target.isNumber()) {
			return new TextNode(maskingEngine.maskNumber(target.asText()));
			
		}

		if (target.isObject()) {
			Iterator<Map.Entry<String, JsonNode>> fields = target.fields();
			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> field = fields.next();
				if (!whitelistedKeys.contains(field.getKey().toUpperCase())) {
					String childPath = appendPath(path, field.getKey());
					if (!expandedWhitelistedPaths.contains(childPath)) {
						((ObjectNode) target).replace(field.getKey(),
								traverseAndMask(field.getValue(), expandedWhitelistedPaths, childPath));
					}
				}
			}
		}
		if (target.isArray()) {
			for (int i = 0; i < target.size(); i++) {
				String childPath = appendPath(path, i);
				if (!expandedWhitelistedPaths.contains(childPath)) {
					((ArrayNode) target).set(i, traverseAndMask(target.get(i), expandedWhitelistedPaths, childPath));
				}
			}
		}
		return target;
	}

	private static String appendPath(String path, String key) {
		return path + "['" + key + "']";
	}

	private static String appendPath(String path, int ind) {
		return path + "[" + ind + "]";
	}


	private String prettyPrint(JsonNode jsonNode) {

		if (jsonNode == null)
			throw new InvalidInputException("Pretty print is not applicable to null object");
		
		try {
			
			return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(jsonNode);
				
		} catch (IOException e) {
			throw new InvalidInputException("An exception accurred trying to pretty print the Json",e);
		}
	}


    
	@Override
	public char[] mask(char[] text)  {
		
		if(text == null)
		 throw new InvalidInputException("The char[] provaded is null and cannot be masked");
		
		if(!enabled || text.length == 0 )
			return text;
		
		Result jsons = jsonFinder.find(text);
		
		char[] result = text;
		int deltaLengthAfterMasking = 0;
		for(Text json : jsons.texts()) {
			
			if(CharsUtils.isEmptyJson(json.chars()))
				continue;
			
			char[] masked = prettyPrint(mask(readJson(json.toString()))).toCharArray();
			int lengthBeforeMasking  = result.length;
			result = CharsUtils.replace(result, json.start() + deltaLengthAfterMasking, json.end() + deltaLengthAfterMasking, masked);
			deltaLengthAfterMasking = result.length - lengthBeforeMasking;			
		}
		
		return result;

	}
	

	
	private JsonNode readJson(String json) {
		try {
			return mapper.readTree(json);
			
		} catch (IOException e) {
		 throw new InvalidInputException("The string is not a valid Json",e);
		}

	}
	
	

}