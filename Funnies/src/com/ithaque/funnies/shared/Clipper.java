package com.ithaque.funnies.shared;

import java.util.ArrayList;
import java.util.List;

public class Clipper {

	public class Node {

		public Node() {
		}

		public Node(float x, float y, Node next, Node prev, Node nextPoly,
				Node neighbor, boolean intersect, boolean entry,
				boolean visited, float alpha) {
			super();
			this.x = x;
			this.y = y;
			this.next = next;
			this.prev = prev;
			this.nextPoly = nextPoly;
			this.neighbor = neighbor;
			this.intersect = intersect;
			this.entry = entry;
			this.visited = visited;
			this.alpha = alpha;
			if (prev != null) {
				this.prev.next = this;
			}
			if (next != null) {
				this.next.prev = this;
			}
		}

		float x, y;
		Node next;
		Node prev;
		Node nextPoly; /* pointer to the next polygon */
		Node neighbor; /* the coresponding intersection point */
		boolean intersect; /* 1 if an intersection point, 0 otherwise */
		boolean entry; /* 1 if an entry point, 0 otherwise */
		boolean visited; /* 1 if the node has been visited, 0 otherwise */
		float alpha; /* intersection point placemet */
		
		public String toString() {
			return "["+x+","+y+":"+entry+"]";
		}
		
		public String toShape() {
			String result = toString();
			Node next = this.next;
			while (next!=null && next!=this) {
				result+="->"+next.toString();
				next = next.next;
			}
			return result;
		}
	}

	void insert(Node ins, Node first, Node last) {
		Node aux = first;
		while (aux != last && aux.alpha < ins.alpha) {
			aux = aux.next;
		}
		ins.next = aux;
		ins.prev = aux.prev;
		ins.prev.next = ins;
		ins.next.prev = ins;
	}

	Node nextNode(Node polygon) {
		Node aux = polygon;
		while (aux != null && aux.intersect) {
			aux = aux.next;
		}
		return aux;
	}

	Node lastNode(Node polygon) {
		Node aux = polygon;
		if (aux != null) {
			while (aux.next != null) {
				aux = aux.next;
			}
		}
		return aux;
	}

	Node first(Node polygon) {
		Node aux = polygon;
		if (aux != null) {
			do {
				aux = aux.next;
			} while (aux != polygon
					&& (!aux.intersect || aux.intersect && aux.visited));
		}
		return aux;
	}

	void circle(Node polygon) {
		Node aux = lastNode(polygon);
		aux.prev.next = polygon;
		polygon.prev = aux.prev;
	}

	float dist(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	static class Intersection {
		float alphaP;
		float alphaQ;
		float x;
		float y;
	}

	boolean intersect(Node p1, Node p2, Node q1, Node q2, Intersection result) {
		float par = (float) ((p2.x - p1.x) * (q2.y - q1.y) - (p2.y - p1.y) * (q2.x - q1.x));
		if (par == 0.0f) {
			return false; /* parallel lines */
		}
		float tp = ((q1.x - p1.x) * (q2.y - q1.y) - (q1.y - p1.y) * (q2.x - q1.x))/ par;
		float tq = ((p2.y - p1.y) * (q1.x - p1.x) - (p2.x - p1.x)* (q1.y - p1.y))/ par;
		if (tp < 0 || tp >= 1 || tq < 0 || tq >= 1) {
			return false;
		}
		result.x = p1.x + tp * (p2.x - p1.x);
		result.y = p1.y + tp * (p2.y - p1.y);
		result.alphaP = dist(p1.x, p1.y, result.x, result.y)
				/ dist(p1.x, p1.y, p2.x, p2.y);
		result.alphaQ = dist(q1.x, q1.y, result.x, result.y)
				/ dist(q1.x, q1.y, q2.x, q2.y);
		return true;
	}

	boolean test(Node point, Node polygon) {
		int type = 0;
		Node left = new Node(-100000, point.y, null, null, null, null, false, false, false, 0.0f);
		Intersection result = new Intersection();
		for (Node aux = polygon; aux.next != null; aux = aux.next) {
			if (intersect(left, point, aux, aux.next, result)) {
				type++;
			}
		}
		return type % 2 != 0;
	}

	Node clip(Node s, Node c, boolean ps, boolean pc) {
		Node is, ic, result = null;
		Intersection its = new Intersection();
		Node crt, newP, oldP;
		if (s == null || c == null) {
			return null;
		}
		Node auxs = lastNode(s);
		new Node(s.x, s.y, null, auxs, null, null, false, false, false, 0.0f);
		Node auxc = lastNode(c);
		new Node(c.x, c.y, null, auxc, null, null, false, false, false, 0.0f);
		for (auxs = s; auxs.next != null; auxs = auxs.next) {
			if (!auxs.intersect) {
				for (auxc = c; auxc.next != null; auxc = auxc.next) {
					if (!auxc.intersect) {
						if (intersect(auxs, nextNode(auxs.next), auxc,
								nextNode(auxc.next), its)) {
							is = new Node(its.x, its.y, null, null, null, null,
									true, false, false, its.alphaP);
							ic = new Node(its.x, its.y, null, null, null, null,
									true, false, false, its.alphaQ);
							is.neighbor = ic;
							ic.neighbor = is;
							insert(is, auxs, nextNode(auxs.next));
							insert(ic, auxc, nextNode(auxc.next));
						}
					}
				}
			}
		}
		boolean entry = test(s, c);
		if (ps) {
			entry = !entry;
		}
		for (auxs = s; auxs.next != null; auxs = auxs.next) {
			auxs.entry = entry;
			if (auxs.intersect) {
				entry = !entry;
			}
		}
		entry = test(c, s);
		if (pc) {
			entry = !entry;
		}
		for (auxc = c; auxc.next != null; auxc = auxc.next) {
			auxc.entry = entry;
			if (auxc.intersect) {
				entry = !entry;
			}
		}
		circle(s);
		circle(c);
		while ((crt = first(s)) != s) {
			oldP = null;
			for (; !crt.visited; crt = crt.neighbor) {
				for (boolean forward = crt.entry;;) {
					newP = new Node(crt.x, crt.y, oldP, null, null, null,
							false, !forward, false, 0.0f);
					oldP = newP;
					crt.visited = true;
					crt = forward ? crt.next : crt.prev;
					if (crt.intersect) {
						crt.visited = true;
						break;
					}
				}
			}
			oldP.nextPoly = result;
			result = oldP;
		}
		return result;
	}

	Node prepare(Shape shape) {
		Node result = null, last = null;
		for (Location location : shape.getLocations()) {
			Node next = new Node(location.x, location.y, null, null, null,
					null, false, false, false, 0.0f);
			if (result == null) {
				result = next;
			} else {
				next.prev = last;
				last.next = next;
			}
			last = next;
		}
		return result;
	}

	List<Shape> exploit(Node root) {
		List<Shape> result = new ArrayList<Shape>();
		Node poly;
		for (poly = root; poly != null; poly = poly.nextPoly) {
			int count = 0;
			boolean forward = poly.entry;
			for (Node aux = poly; aux != null; aux = aux.next) {
				count++;
			}
			Location[] locations = new Location[count];
			int index = 0;
			for (Node aux = poly; aux != null; aux = aux.next) {
				locations[forward?index++:count-++index] = new Location(aux.x, aux.y);
			}
			result.add(new Shape(locations));
		}
		return result;
	}
	
	public List<Shape> clip(Shape subject, Shape clipping, boolean subDir, boolean clipDir) {
		Node s = prepare(subject);
		Node c = prepare(clipping);
		Node result = clip(s, c, subDir, clipDir);
		return exploit(result);
	}
}
