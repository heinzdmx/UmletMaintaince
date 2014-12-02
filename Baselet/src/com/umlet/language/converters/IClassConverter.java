package com.umlet.language.converters;


public interface IClassConverter<TypeParsed,TypeSource> {
	public static final String SPLITTER = "--\n";

	boolean canConvert(String extension);

	String getElementProperties(String file);

	void createTopSection(TypeParsed parsedClass, StringBuilder attributes);

	void createFieldSection(TypeParsed parsedClass, StringBuilder attributes);

	void createMethodSection(TypeParsed parsedClass, StringBuilder attributes);

	SourceObject<TypeSource> parseElementProperties(String attributes);

	void parseTopSection(SourceObject<TypeSource> code, String attributes);

	void parseFieldSection(SourceObject<TypeSource> code, String attributes);

	void parseMethodSection(SourceObject<TypeSource> code, String attributes);

}