package com.umlet.language;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.baselet.element.GridElement;

public class ClassCodeConverter {

	public void createCodeDiagrams(String path, List<GridElement> elements) {
		if (path == null || elements == null) {
			return;
		}

		List<String> files = new ArrayList<String>();

		for (GridElement e : elements)
		{
			if (e instanceof com.umlet.element.Class)
			{
				for (String attribute : e.getPanelAttributesAsList())
				{
					attribute = attribute.replace("'", "").trim();

					if (attribute.startsWith("<<") || // stereotype
						attribute.startsWith("//") || attribute.startsWith("/*") || // comments
						attribute.isEmpty()) {
						continue;
					}

					if (attribute.startsWith("/") && attribute.endsWith("/")) {
						attribute = attribute.substring(1, attribute.length() - 1);
					}
					attribute = attribute.replace("::", File.separator);
					attribute = attribute.replace(":", File.separator);
					attribute = attribute.replace(".", File.separator);
					attribute += ".java";

					files.add(attribute);
					break;
				}
			}
		}

		File dir = new File(path);

		if (dir.exists() || dir.mkdirs())
		{
			for (String file : files)
			{
				File f = new File(dir, file);
				File parent = f.getParentFile();

				if (parent.exists() || parent.mkdirs())
				{
					try {
						f.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
