package com.ithaque.funnies.shared;

import com.ithaque.funnies.shared.basic.Location;


public class Transform {

	public float m[] = { 1, 0, 0, 1, 0, 0 };

	public Transform reset() {
		this.m[0] = 1;
		this.m[1] = 0;
		this.m[2] = 0;
		this.m[3] = 1;
		this.m[4] = 0;
		this.m[5] = 0;
		return this;
	};

	public Transform multiply(Transform matrix) {
		float m11 = this.m[0] * matrix.m[0] + this.m[2] * matrix.m[1];
		float m12 = this.m[1] * matrix.m[0] + this.m[3] * matrix.m[1];

		float m21 = this.m[0] * matrix.m[2] + this.m[2] * matrix.m[3];
		float m22 = this.m[1] * matrix.m[2] + this.m[3] * matrix.m[3];

		float dx = this.m[0] * matrix.m[4] + this.m[2] * matrix.m[5]
				+ this.m[4];
		float dy = this.m[1] * matrix.m[4] + this.m[3] * matrix.m[5]
				+ this.m[5];

		this.m[0] = m11;
		this.m[1] = m12;
		this.m[2] = m21;
		this.m[3] = m22;
		this.m[4] = dx;
		this.m[5] = dy;
		return this;
	};

	public Transform invert() {
		float d = 1 / (this.m[0] * this.m[3] - this.m[1] * this.m[2]);
		float m0 = this.m[3] * d;
		float m1 = -this.m[1] * d;
		float m2 = -this.m[2] * d;
		float m3 = this.m[0] * d;
		float m4 = d * (this.m[2] * this.m[5] - this.m[3] * this.m[4]);
		float m5 = d * (this.m[1] * this.m[4] - this.m[0] * this.m[5]);
		this.m[0] = m0;
		this.m[1] = m1;
		this.m[2] = m2;
		this.m[3] = m3;
		this.m[4] = m4;
		this.m[5] = m5;
		return this;
	};

	public Transform rotate(float rad) {
		float c = (float) Math.cos(rad);
		float s = (float) Math.sin(rad);
		float m11 = this.m[0] * c + this.m[2] * s;
		float m12 = this.m[1] * c + this.m[3] * s;
		float m21 = this.m[0] * -s + this.m[2] * c;
		float m22 = this.m[1] * -s + this.m[3] * c;
		this.m[0] = m11;
		this.m[1] = m12;
		this.m[2] = m21;
		this.m[3] = m22;
		return this;
	};

	public Transform translate(float x, float y) {
		this.m[4] += this.m[0] * x + this.m[2] * y;
		this.m[5] += this.m[1] * x + this.m[3] * y;
		return this;
	};

	public Transform scale(float sx, float sy) {
		this.m[0] *= sx;
		this.m[1] *= sx;
		this.m[2] *= sy;
		this.m[3] *= sy;
		return this;
	};
	
	public Location transformPoint(Location location) {
		float x = location.getX();
		float y = location.getY();
		float px = x * this.m[0] + y * this.m[2] + this.m[4];
		float py = x * this.m[1] + y * this.m[3] + this.m[5];
		return new Location(px, py);
	};
	
}
