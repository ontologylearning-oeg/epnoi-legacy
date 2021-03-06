package org.epnoi.uia.informationstore.dao.rdf;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import org.epnoi.model.Context;
import org.epnoi.model.InformationSource;
import org.epnoi.model.Resource;
import org.epnoi.model.Term;
import org.epnoi.model.rdf.RDFHelper;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import java.util.Iterator;

public class TermRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Term term = (Term) resource;
		String termURI = term.getUri();

		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{TERM_CLASS}> . }";

		queryExpression = queryExpression
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", termURI)
				.replace("{TERM_CLASS}", RDFHelper.TERM_CLASS);

	
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, this.graph);

		vur.exec();

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(InformationSource informationSource) {
		// TODO Update operation for the Term
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void remove(String URI) {

		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		// System.out.println("\nDESCRIBE results:");
		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple triple = (Triple) i.next();

			this.graph.remove(triple);

		}
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public Resource read(String URI) {
		Term user = new Term();
		user.setUri(URI);

		return user;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(Resource resource) {

	}

	// ---------------------------------------------------------------------------------------------------------------------

}
