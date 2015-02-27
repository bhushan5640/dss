/*
 * DSS - Digital Signature Services
 *
 * Copyright (C) 2013 European Commission, Directorate-General Internal Market and Services (DG MARKT), B-1049 Bruxelles/Brussel
 *
 * Developed by: 2013 ARHS Developments S.A. (rue Nicolas Bové 2B, L-1253 Luxembourg) http://www.arhs-developments.com
 *
 * This file is part of the "DSS - Digital Signature Services" project.
 *
 * "DSS - Digital Signature Services" is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * DSS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * "DSS - Digital Signature Services".  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.markt.dss.signature.xades;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import eu.europa.ec.markt.dss.DSSXMLUtils;
import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ec.markt.dss.parameter.DSSReference;
import eu.europa.ec.markt.dss.parameter.SignatureParameters;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.validation102853.CertificateVerifier;

/**
 * This class handles the specifics of the detached XML signature.
 */
class DetachedSignatureBuilder extends SignatureBuilder {

	/**
	 * The default constructor for DetachedSignatureBuilder.<br>
	 * The detached signature uses by default the exclusive method of canonicalization.
	 *  @param params  The set of parameters relating to the structure and process of the creation or extension of the
	 *                electronic signature.
	 * @param origDoc The original document to sign.
	 * @param certificateVerifier
	 */
	public DetachedSignatureBuilder(final SignatureParameters params, final DSSDocument origDoc, final CertificateVerifier certificateVerifier) {
		super(params, origDoc, certificateVerifier);
		setCanonicalizationMethods(params, CanonicalizationMethod.EXCLUSIVE);
	}


	@Override
	protected Document buildRootDocumentDom() {
		if (params.getRootDocumentXAdES() != null){
			return params.getRootDocumentXAdES();
		}
		return DSSXMLUtils.buildDOM();
	}

	@Override
	protected Node getParentNodeOfSignature() {
		if (params.getRootDocumentXAdES() != null){
			return documentDom.getDocumentElement();
		}
		return documentDom;
	}

	@Override
	protected List<DSSReference> createDefaultReferences() {

		final List<DSSReference> references = new ArrayList<DSSReference>();

		DSSDocument currentDetachedDocument = detachedDocument;
		int referenceIndex = 1;
		do {
			//<ds:Reference Id="detached-ref-id" URI="xml_example.xml">
			final DSSReference reference = new DSSReference();
			reference.setId("r-id-" + referenceIndex++);
			final String fileURI = currentDetachedDocument.getName() != null ? currentDetachedDocument.getName() : "";
			reference.setUri(fileURI);
			reference.setContents(currentDetachedDocument);
			reference.setDigestMethodAlgorithm(params.getDigestAlgorithm());

			references.add(reference);
			currentDetachedDocument = currentDetachedDocument.getNextDocument();
		} while (currentDetachedDocument != null);
		return references;
	}

	/**
	 * This method creates the first reference (this is a reference to the file to sign) which is specific for each form
	 * of signature. Here, the value of the URI is the name of the file to sign or if the information is not available
	 * the URI will use the default value: "detached-file".
	 *
	 * @throws DSSException
	 */
	@Override
	protected void incorporateReferences() throws DSSException {
		final List<DSSReference> references = params.getReferences();
		for (final DSSReference reference : references) {
			incorporateReference(reference);
		}
	}

	@Override
	protected DSSDocument transformReference(final DSSReference reference) {
		return reference.getContents();
	}

}