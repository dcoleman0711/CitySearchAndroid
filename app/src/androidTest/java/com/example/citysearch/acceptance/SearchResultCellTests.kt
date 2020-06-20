package com.example.citysearch.acceptance

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.citysearch.data.CitySearchResult
import com.example.citysearch.reactive.ViewBinderImp
import com.example.citysearch.search.OpenDetailsCommand
import com.example.citysearch.search.OpenDetailsCommandFactory
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultCell
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultModelFactoryImp
import com.example.citysearch.search.searchresults.citysearchresultcell.CitySearchResultViewModelFactoryImp
import com.example.citysearch.stub.CitySearchResultsStub
import com.example.citysearch.utilities.ConstraintSetFactoryImp
import com.example.citysearch.utilities.ImageLoader
import com.example.citysearch.utilities.MeasureConverterImp
import com.example.citysearch.utilities.Size
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchResultCellTests {

    lateinit var steps: SearchResultCellSteps

    val Given: SearchResultCellSteps get() = steps
    val When: SearchResultCellSteps get() = steps
    val Then: SearchResultCellSteps get() = steps

    @Before
    fun setUp() {

        steps = SearchResultCellSteps(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun testTitleBottomCenter() {

        val cellSizes = Given.cellSizes()

        for(cellSize in cellSizes) {

            testTitleBottomCenter(cellSize)
            setUp()
        }
    }

    fun testTitleBottomCenter(cellSize: Size) {

        val titleLabel = Given.titleLabel()
        val searchResultCell = Given.searchResultCellIsCreated(titleLabel = titleLabel)

        When.cellSizeBecomes(searchResultCell, cellSize)

        Then.titleLabelIsBottomCenteredIn(titleLabel, searchResultCell)
    }

    @Test
    fun testTitleFitsText() {

        val cellSizes = Given.cellSizes()

        for(cellSize in cellSizes) {

            testTitleFitsText(cellSize)
            setUp()
        }
    }

    fun testTitleFitsText(cellSize: Size) {

        val titleText = Given.titleText()
        val titleLabel = Given.titleLabel(titleText)
        val searchResultCell = Given.searchResultCellIsCreated(titleLabel = titleLabel)

        When.cellSizeBecomes(searchResultCell, cellSize)

        Then.titleLabelFitsWidthOfCell(titleLabel, searchResultCell)
    }

    @Test
    fun testCellTitleLabelText() {

        val searchResult = Given.searchResult()
        val titleText = Given.titleText(searchResult)
        val titleLabel = Given.titleLabel()
        val searchResultCell = Given.searchResultCellIsCreated(titleLabel = titleLabel)

        When.assignResultToCell(searchResult, searchResultCell)

        Then.titleLabelTextIs(titleLabel, titleText)
    }

    @Test
    fun testImageViewSquare() {

        val cellSizes = Given.cellSizes()

        for(cellSize in cellSizes) {

            testImageViewSquare(cellSize)
            setUp()
        }
    }

    fun testImageViewSquare(cellSize: Size) {

        val imageView = Given.imageView()
        val searchResultCell = Given.searchResultCellIsCreated(imageView = imageView)

        When.cellSizeBecomes(searchResultCell, cellSize)

        Then.imageViewIsSquare(imageView)
    }

    @Test
    fun testImageViewCenteredHorizontally() {

        val cellSizes = Given.cellSizes()

        for(cellSize in cellSizes) {

            testImageViewCenteredHorizontally(cellSize)
            setUp()
        }
    }

    fun testImageViewCenteredHorizontally(cellSize: Size) {

        val titleLabel = Given.titleLabel()
        val imageView = Given.imageView()
        val searchResultCell = Given.searchResultCellIsCreated(titleLabel = titleLabel, imageView = imageView)

        When.cellSizeBecomes(searchResultCell, cellSize)

        Then.imageViewIsCenteredHorizontallyIn(imageView, searchResultCell)
    }

    @Test
    fun testImageViewFitsVertically() {

        val cellSizes = Given.cellSizes()

        for(cellSize in cellSizes) {

            testImageViewFitsVertically(cellSize)
            setUp()
        }
    }

    fun testImageViewFitsVertically(cellSize: Size) {

        val titleLabel = Given.titleLabel()
        val imageView = Given.imageView()
        val searchResultCell = Given.searchResultCellIsCreated(titleLabel = titleLabel, imageView = imageView)

        When.cellSizeBecomes(searchResultCell, cellSize)

        Then.imageViewIsFittedFromTopOfCellToTopOfTitle(imageView, searchResultCell, titleLabel)
    }

    @Test
    fun testImageViewCornerRadius() {

        val imageView = Given.imageView()
        val cornerRadius = Given.cornerRadius()

        val searchResultCell = When.searchResultCellIsCreated(imageView = imageView)

        Then.imageViewCornerRadiusIs(imageView, cornerRadius)
    }

    @Test
    fun testImageViewImageIsAspectFill() {

        val imageView = Given.imageView()

        val searchResultCell = When.searchResultCellIsCreated(imageView = imageView)

        Then.imageViewDisplayImageWithAspectFill(imageView)
    }
}

class SearchResultCellSteps(private val context: Context) {

    private val titleLabel = TextView(context)
    private val imageView = ImageView(context)

    private val measureConverter = MeasureConverterImp(context)

    fun searchResult(): CitySearchResult {

        return CitySearchResultsStub.stubResults().results[0]
    }

    fun titleText(searchResult: CitySearchResult): String {

        return searchResult.name
    }

    fun titleText(): String {

        return "Test Title"
    }

    fun titleLabel(text: String = ""): TextView {

        titleLabel.text = text
        return titleLabel
    }

    fun imageView(): ImageView {

        return imageView
    }

    fun cornerRadius(): Int {

        return 52
    }

    fun cellSizes(): List<Size> {

        return arrayListOf(Size(128, 128), Size(64, 64), Size(32, 32))
    }

    fun searchResultCellIsCreated(titleLabel: TextView = this.titleLabel, imageView: ImageView = this.imageView): CitySearchResultCell {

        imageView.setImageBitmap(ImageLoader.loadImage(context, "TestImage.jpg"))
        return CitySearchResultCell(context, titleLabel, imageView, ViewBinderImp(), ConstraintSetFactoryImp(), measureConverter)
    }

    fun assignResultToCell(result: CitySearchResult, cell: CitySearchResultCell) {

        val modelFactory = CitySearchResultModelFactoryImp()
        val viewModelFactory = CitySearchResultViewModelFactoryImp()

        val openDetailsCommand = mock<OpenDetailsCommand>()
        val openDetailsCommandFactory = mock<OpenDetailsCommandFactory> {

            on { openDetailsCommand(any()) }.thenReturn(openDetailsCommand)
        }

        val model = modelFactory.resultModel(result, openDetailsCommandFactory)
        val viewModel = viewModelFactory.resultViewModel(context, model)

        cell.viewModel = viewModel
    }

    fun cellSizeBecomes(cell: CitySearchResultCell, size: Size) {

        val width = measureConverter.convertToPixels(size.width)
        val height = measureConverter.convertToPixels(size.height)

        cell.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
        cell.layout(0, 0, width, height)
    }

    fun titleLabelTextIs(titleLabel: TextView, expectedText: String) {

        Assert.assertEquals("Title label text is not expected text", expectedText, titleLabel.text)
    }

    fun titleLabelIsBottomCenteredIn(titleLabel: TextView, cell: CitySearchResultCell) {

        Assert.assertEquals("Title label is not horizontally centered in frame", cell.width / 2, (titleLabel.left + titleLabel.right) / 2)
        Assert.assertEquals("Title label is not in bottom of frame", cell.height, titleLabel.bottom)
    }

    fun titleLabelFitsWidthOfCell(titleLabel: TextView, cell: CitySearchResultCell) {

        Assert.assertEquals("Title label size does not fit cell width", cell.width, titleLabel.width)
    }

    fun imageViewIsSquare(imageView: ImageView) {

        Assert.assertTrue("Image view is not square", imageView.height == imageView.width)
    }

    fun imageViewIsCenteredHorizontallyIn(imageView: ImageView, cell: CitySearchResultCell) {

        Assert.assertEquals("Image view top is not centered horizontally in cell", cell.width / 2, (imageView.left + imageView.right) / 2)
    }

    fun imageViewIsFittedFromTopOfCellToTopOfTitle(imageView: ImageView, cell: CitySearchResultCell, titleLabel: TextView) {

        Assert.assertEquals("Image view top is not cell top", 0, imageView.top)
        Assert.assertEquals("Image view bottom is not title label top", titleLabel.top, imageView.bottom)
    }

    fun imageViewCornerRadiusIs(imageView: ImageView, cornerRadius: Int) {

        Assert.assertTrue("Image view must be masked to bounds to have rounded corners", imageView.clipToOutline)
        Assert.assertEquals("Image view corner radius is not correct", measureConverter.convertToPixels(cornerRadius).toFloat(), (imageView.background as GradientDrawable).cornerRadius)
    }

    fun imageViewDisplayImageWithAspectFill(imageView: ImageView) {

        Assert.assertEquals("Image view does not display image with aspect fill", ImageView.ScaleType.CENTER_CROP, imageView.scaleType)
    }
}