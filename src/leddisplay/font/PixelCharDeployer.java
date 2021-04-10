package leddisplay.font;

public class PixelCharDeployer {
	private final PixelFontMetrics metrics;
	private int targetWidth, targetHeight;
	
	private HorizontalDeployment horizontalDeployment = HorizontalDeployment.CENTER;
	private VerticalDeployment verticalDeployment = VerticalDeployment.BOTTOM_FONT_DESCENT;
	
	public PixelCharDeployer(PixelFontMetrics metrics, int targetWidth, int targetHeight) {
		super();
		this.metrics = metrics;
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
	}

	public PixelChar deploy(PixelChar pixelChar) {
		PixelsMatrix matrix = new PixelsMatrix(pixelChar);
		PixelChar deployed = deployPrivate(matrix);
		return new IndexOutOfBoundsGuardPixelChar(deployed);
	}

	private PixelChar deployPrivate(PixelsMatrix matrix) {
		if (matrix.getEmptyColumnsCountLeft() != matrix.getWidth()) {
			return deployNotEmpty(matrix);
		} else {
			return deployEmpty(matrix);
		}
	}
	
	private PixelChar deployNotEmpty(PixelsMatrix matrix) {
		HorizontalDeployer horizontalDeployer = new HorizontalDeployer(horizontalDeployment, targetWidth);
		matrix = horizontalDeployer.deploy(matrix);
		VerticalDeployer verticalDeployer = new VerticalDeployer(verticalDeployment, metrics, targetHeight);
		return verticalDeployer.deploy(matrix);
	}
	
	// PixelMatrix not supports 0 dimension
	private PixelChar deployEmpty(PixelsMatrix matrix) {
		// guard
		return matrix;
	}

	public HorizontalDeployment getHorizontalDeployment() {
		return horizontalDeployment;
	}

	public void setHorizontalDeployment(HorizontalDeployment horizontalDeployment) {
		this.horizontalDeployment = horizontalDeployment;
	}

	public VerticalDeployment getVerticalDeployment() {
		return verticalDeployment;
	}

	public void setVerticalDeployment(VerticalDeployment verticalDeployment) {
		this.verticalDeployment = verticalDeployment;
	}

	public int getTargetWidth() {
		return targetWidth;
	}

	public void setTargetWidth(int targetWidth) {
		this.targetWidth = targetWidth;
	}

	public int getTargetHeight() {
		return targetHeight;
	}

	public void setTargetHeight(int targetHeight) {
		this.targetHeight = targetHeight;
	}
	
}