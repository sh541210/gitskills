package com.iyin.sign.system.util.sign;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiepengbin
 */
public class IyinTextLocationExtractionStrategy extends LocationTextExtractionStrategy {
	
	static Logger logger=LoggerFactory.getLogger(IyinTextLocationExtractionStrategy.class);
	static Field locationalResultField = null;
	static Method filterTextChunksMethod = null;
	static Method startsWithSpaceMethod = null;
	static Method endsWithSpaceMethod = null;
	static Field textChunkTextField = null;
	static Method textChunkSameLineMethod = null;
	
	static {
		try {
			locationalResultField = LocationTextExtractionStrategy.class.getDeclaredField("locationalResult");
			locationalResultField.setAccessible(true);
			filterTextChunksMethod = LocationTextExtractionStrategy.class.getDeclaredMethod("filterTextChunks",
					List.class, TextChunkFilter.class);
			filterTextChunksMethod.setAccessible(true);
			startsWithSpaceMethod = LocationTextExtractionStrategy.class.getDeclaredMethod("startsWithSpace",
					String.class);
			startsWithSpaceMethod.setAccessible(true);
			endsWithSpaceMethod = LocationTextExtractionStrategy.class.getDeclaredMethod("endsWithSpace", String.class);
			endsWithSpaceMethod.setAccessible(true);
			textChunkTextField = TextChunk.class.getDeclaredField("text");
			textChunkTextField.setAccessible(true);
			textChunkSameLineMethod = TextChunk.class.getDeclaredMethod("sameLine", TextChunk.class);
			textChunkSameLineMethod.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
	}
	
	public IyinTextLocationExtractionStrategy(Pattern pattern) {
		super(new TextChunkLocationStrategy() {
			@Override
			public TextChunkLocation createLocation(TextRenderInfo renderInfo, LineSegment baseline) {
				// while baseLine has been changed to not neutralize
				// effects of rise, ascentLine and descentLine explicitly
				// have not: We want the actual positions.
				return new AscentDescentTextChunkLocation(baseline, renderInfo.getAscentLine(),
						renderInfo.getDescentLine(), renderInfo.getSingleSpaceWidth());
			}
		});
		this.pattern = pattern;
	}

	@SuppressWarnings("unchecked")
	public List<TextRectangle> getLocations(TextChunkFilter chunkFilter,int pageNo) {
		List<TextRectangle> result=new ArrayList<TextRectangle>();
		try {
			List<TextChunk> filteredTextChunks = (List<TextChunk>) filterTextChunksMethod.invoke(this,
					locationalResultField.get(this), chunkFilter);
			Collections.sort(filteredTextChunks);

			StringBuilder sb = new StringBuilder();
			List<AscentDescentTextChunkLocation> locations = new ArrayList<>();
			TextChunk lastChunk = null;
			for (TextChunk chunk : filteredTextChunks) {
				String chunkText = (String) textChunkTextField.get(chunk);
				if (lastChunk == null) {
					// Nothing to compare with at the end
				} else if ((boolean) textChunkSameLineMethod.invoke(chunk, lastChunk)) {
					// we only insert a blank space if the trailing character of the previous string
					// wasn't a space,
					// and the leading character of the current string isn't a space
					if (isChunkAtWordBoundary(chunk, lastChunk)
							&& !((boolean) startsWithSpaceMethod.invoke(this, chunkText))
							&& !((boolean) endsWithSpaceMethod.invoke(this, chunkText))) {
						sb.append(' ');
						LineSegment spaceBaseLine = new LineSegment(lastChunk.getEndLocation(),
								chunk.getStartLocation());
						locations.add(new AscentDescentTextChunkLocation(spaceBaseLine, spaceBaseLine, spaceBaseLine,
								chunk.getCharSpaceWidth()));
					}
				} else {
					assert sb.length() == locations.size();
					Matcher matcher = pattern.matcher(sb);
					while (matcher.find()) {
						int i = matcher.start();
						Vector baseStart = locations.get(i).getStartLocation();
						TextRectangle textRectangle = new TextRectangle(matcher.group(), baseStart.get(Vector.I1),
								baseStart.get(Vector.I2),pageNo);
						for (; i < matcher.end(); i++) {
							AscentDescentTextChunkLocation location = locations.get(i);
							textRectangle.add(location.getAscentLine().getBoundingRectange());
							textRectangle.add(location.getDescentLine().getBoundingRectange());
						}
						textRectangle.x=textRectangle.x+textRectangle.width/2;
						textRectangle.y=textRectangle.y+textRectangle.height/2;
						result.add(textRectangle);
					}

					sb.setLength(0);
					locations.clear();
				}
				sb.append(chunkText);
				locations.add((AscentDescentTextChunkLocation) chunk.getLocation());
				lastChunk = chunk;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
		return result;
	}

	@Override
	public void renderText(TextRenderInfo renderInfo) {
		for (TextRenderInfo info : renderInfo.getCharacterRenderInfos()) {
			super.renderText(info);
		}
	}

	private static class IyinTextChunkLocationDefaultImp implements TextChunkLocation {

		/** the starting location of the chunk */
		private final Vector startLocation;
		/** the ending location of the chunk */
		private final Vector endLocation;
		/** unit vector in the orientation of the chunk */
		private final Vector orientationVector;
		/** the orientation as a scalar for quick sorting */
		private final int orientationMagnitude;
		/**
		 * perpendicular distance to the orientation unit vector (i.e. the Y position in
		 * an unrotated coordinate system) we round to the nearest integer to handle the
		 * fuzziness of comparing floats
		 */
		private final int distPerpendicular;
		/**
		 * distance of the start of the chunk parallel to the orientation unit vector
		 * (i.e. the X position in an unrotated coordinate system)
		 */
		private final float distParallelStart;
		/**
		 * distance of the end of the chunk parallel to the orientation unit vector
		 * (i.e. the X position in an unrotated coordinate system)
		 */
		private final float distParallelEnd;
		/** the width of a single space character in the font of the chunk */
		private final float charSpaceWidth;

		public IyinTextChunkLocationDefaultImp(Vector startLocation, Vector endLocation, float charSpaceWidth) {
			this.startLocation = startLocation;
			this.endLocation = endLocation;
			this.charSpaceWidth = charSpaceWidth;

			Vector oVector = endLocation.subtract(startLocation);
			if (oVector.length() == 0) {
				oVector = new Vector(1, 0, 0);
			}
			orientationVector = oVector.normalize();
			orientationMagnitude = (int) (Math.atan2(orientationVector.get(Vector.I2), orientationVector.get(Vector.I1))
					* 1000);

			// see http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
			// the two vectors we are crossing are in the same plane, so the result will be
			// purely
			// in the z-axis (out of plane) direction, so we just take the I3 component of
			// the result
			Vector origin = new Vector(0, 0, 1);
			distPerpendicular = (int) (startLocation.subtract(origin)).cross(orientationVector).get(Vector.I3);

			distParallelStart = orientationVector.dot(startLocation);
			distParallelEnd = orientationVector.dot(endLocation);
		}

		@Override
		public int orientationMagnitude() {
			return orientationMagnitude;
		}

		@Override
		public int distPerpendicular() {
			return distPerpendicular;
		}

		@Override
		public float distParallelStart() {
			return distParallelStart;
		}

		@Override
		public float distParallelEnd() {
			return distParallelEnd;
		}

		/**
		 * @return the start location of the text
		 */
		@Override
		public Vector getStartLocation() {
			return startLocation;
		}

		/**
		 * @return the end location of the text
		 */
		@Override
		public Vector getEndLocation() {
			return endLocation;
		}

		/**
		 * @return the width of a single space character as rendered by this chunk
		 */
		@Override
		public float getCharSpaceWidth() {
			return charSpaceWidth;
		}

		/**
		 * @param as the location to compare to
		 * @return true is this location is on the the same line as the other
		 */
		@Override
		public boolean sameLine(TextChunkLocation as) {
			return orientationMagnitude() == as.orientationMagnitude() && distPerpendicular() == as.distPerpendicular();
		}

		/**
		 * Computes the distance between the end of 'other' and the beginning of this
		 * chunk in the direction of this chunk's orientation vector. Note that it's a
		 * bad idea to call this for chunks that aren't on the same line and
		 * orientation, but we don't explicitly check for that condition for performance
		 * reasons.
		 * 
		 * @param other
		 * @return the number of spaces between the end of 'other' and the beginning of
		 *         this chunk
		 */
		@Override
		public float distanceFromEndOf(TextChunkLocation other) {
			float distance = distParallelStart() - other.distParallelEnd();
			return distance;
		}

		@Override
		public boolean isAtWordBoundary(TextChunkLocation previous) {
			/**
			 * Here we handle a very specific case which in PDF may look like: -.232 Tc [(
			 * P)-226.2(r)-231.8(e)-230.8(f)-238(a)-238.9(c)-228.9(e)]TJ The font's
			 * charSpace width is 0.232 and it's compensated with charSpacing of 0.232. And
			 * a resultant TextChunk.charSpaceWidth comes to TextChunk constructor as 0. In
			 * this case every chunk is considered as a word boundary and space is added. We
			 * should consider charSpaceWidth equal (or close) to zero as a no-space.
			 */
			if (getCharSpaceWidth() < 0.1f) {
				return false;
			}

			float dist = distanceFromEndOf(previous);

			return dist < -getCharSpaceWidth() || dist > getCharSpaceWidth() / 2.0f;
		}

		@Override
		public int compareTo(TextChunkLocation other) {
			if (this == other) {
				return 0;
			}

			int rslt;
			rslt = compareInts(orientationMagnitude(), other.orientationMagnitude());
			if (rslt != 0) {
				return rslt;
			}

			rslt = compareInts(distPerpendicular(), other.distPerpendicular());
			if (rslt != 0) {
				return rslt;
			}

			return Float.compare(distParallelStart(), other.distParallelStart());
		}
	}

	private static int compareInts(int int1, int int2) {
		return int1 == int2 ? 0 : int1 < int2 ? -1 : 1;
	}

	public static class AscentDescentTextChunkLocation extends IyinTextChunkLocationDefaultImp {
		public AscentDescentTextChunkLocation(LineSegment baseLine, LineSegment ascentLine, LineSegment descentLine,
				float charSpaceWidth) {
			super(baseLine.getStartPoint(), baseLine.getEndPoint(), charSpaceWidth);
			this.ascentLine = ascentLine;
			this.descentLine = descentLine;
		}

		public LineSegment getAscentLine() {
			return ascentLine;
		}

		public LineSegment getDescentLine() {
			return descentLine;
		}

		final LineSegment ascentLine;
		final LineSegment descentLine;
	}

	public static class TextRectangle extends Rectangle2D.Float {
		final String text;
		final int pageNo;
		final float xStart;
		final float yStart;

		public TextRectangle(final String text, final float xStart, final float yStart,final int pageNo) {
			super(xStart, yStart, 0, 0);
			this.text = text;
			this.pageNo=pageNo;
			this.xStart = xStart;
			this.yStart = yStart;
		}

		public String getText() {
			return text;
		}

		public int getPageNo() {
			return pageNo;
		}

		public float getxStart() {return xStart;}

		public float getyStart() {return yStart;}
	}

	final Pattern pattern;
}