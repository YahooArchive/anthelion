/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 */
package com.yahoo.research.petar.any23;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.any23.Any23;
import org.apache.any23.extractor.ExtractionException;
import org.apache.any23.filter.IgnoreAccidentalRDFa;
import org.apache.any23.filter.IgnoreTitlesOfEmptyDocuments;
import org.apache.any23.source.ByteArrayDocumentSource;
import org.apache.any23.source.DocumentSource;
import org.apache.any23.writer.NTriplesWriter;
import org.apache.any23.writer.TripleHandler;
import org.apache.any23.writer.TripleHandlerException;

/**
 * THIS IS NOT USED
 * 
 * @author Petar Ristoski (petar@dwslab.de)
 *
 */
public class Any23Extractor {

	/**
	 * it extracts triples from a given page content
	 * 
	 * @param content
	 * @return triples in a single string, if the string is null, means there
	 *         are no triples on the web page
	 */
	public static String getTriplesFromContent(ByteArrayDocumentSource content) {

		Any23 runner = new Any23();
		runner.setHTTPUserAgent("test-user-agent");
		// DocumentSource source = null;
		// try {
		// source = new ByteArrayDocumentSource(content.getBytes(),
		// "http://www.test.de",
		// "application/xhtml+xml;q=0.1");
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		TripleHandler handler = new NTriplesWriter(out);
		BasicTripleHandler tripleHandler = new BasicTripleHandler(
				new IgnoreAccidentalRDFa(new IgnoreTitlesOfEmptyDocuments(handler), true),
				BasicTripleHandler.evilNamespaces, BasicTripleHandler.notSoEvilNamespaces);
		// TripleHandler tripleHandler = new ReportingTripleHandler(
		// new IgnoreAccidentalRDFa(new IgnoreTitlesOfEmptyDocuments(handler),
		// true));
		try {
			runner.extract(content, tripleHandler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (ExtractionException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} finally {
			try {
				tripleHandler.close();
			} catch (TripleHandlerException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		String n3 = null;
		try {
			n3 = out.toString("UTF-8");
			if (n3 != null && n3.length() < 3)
				n3 = null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return n3;
	}

	public static void main(String[] args) {

		String documentContent = "";
		try {
			documentContent = readFile("/Users/petar/test.html");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Any23 runner = new Any23();
		runner.setHTTPUserAgent("test-user-agent");
		DocumentSource source = null;
		try {
			source = new ByteArrayDocumentSource(documentContent.getBytes(), "http://www.test.de",
					"application/xhtml+xml;q=0.1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		TripleHandler handler = new NTriplesWriter(out);
		BasicTripleHandler tripleHandler = new BasicTripleHandler(
				new IgnoreAccidentalRDFa(new IgnoreTitlesOfEmptyDocuments(handler), true),
				BasicTripleHandler.evilNamespaces, BasicTripleHandler.notSoEvilNamespaces);
		// TripleHandler tripleHandler = new ReportingTripleHandler(
		// new IgnoreAccidentalRDFa(new IgnoreTitlesOfEmptyDocuments(handler),
		// true));
		try {
			runner.extract(source, tripleHandler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (ExtractionException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} finally {
			try {
				tripleHandler.close();
			} catch (TripleHandlerException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		String n3 = "";
		try {
			n3 = out.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(n3);
	}

	public static String readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}
}
