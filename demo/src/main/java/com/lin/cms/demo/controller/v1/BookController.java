package com.lin.cms.demo.controller.v1;

import com.lin.cms.demo.model.BookDO;
import com.lin.cms.demo.service.BookService;
import com.lin.cms.demo.dto.book.CreateOrUpdateBookDTO;
import com.lin.cms.core.annotation.GroupRequired;
import com.lin.cms.core.annotation.RouteMeta;
import com.lin.cms.demo.vo.CommonResult;
import com.lin.cms.exception.NotFoundException;
import com.lin.cms.demo.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/v1/book")
@Validated
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/{id}")
    public BookDO getBook(@PathVariable(value = "id") @Positive(message = "id必须为正整数") Long id) {
        BookDO book = bookService.findOneByIdAndDeleteTime(id);
        if (book == null) {
            throw new NotFoundException("未找到相关书籍");
        }
        return book;
    }

    @GetMapping("")
    public CommonResult<String> getBooks() {
        // List<BookDO> books = bookService.findAll();
        return ResultUtil.generateSuccessResult("请求成功");
    }


    @GetMapping("/search/one")
    public BookDO searchBook(@RequestParam("q") String q) {
        BookDO book = bookService.getBookByKeyword("%" + q + "%");
        if (book == null) {
            throw new NotFoundException("未找到相关书籍");
        }
        return book;
    }


    @PostMapping("")
    public CommonResult createBook(@RequestBody @Validated CreateOrUpdateBookDTO validator) {
        bookService.createBook(validator);
        return ResultUtil.generateSuccessResult("新建图书成功");
    }


    @PutMapping("/{id}")
    public CommonResult updateBook(@PathVariable("id") @Positive(message = "id必须为正整数") Long id, @RequestBody @Validated CreateOrUpdateBookDTO validator) {
        BookDO book = bookService.findOneByIdAndDeleteTime(id);
        if (book == null) {
            throw new NotFoundException("未找到相关书籍");
        }
        bookService.updateBook(book, validator);
        return ResultUtil.generateSuccessResult("更新图书成功");
    }


    @DeleteMapping("/{id}")
    @RouteMeta(permission = "删除图书", module = "图书", mount = true)
    @GroupRequired
    public CommonResult deleteBook(@PathVariable("id") @Positive(message = "id必须为正整数") Long id) {
        BookDO book = bookService.findOneByIdAndDeleteTime(id);
        if (book == null) {
            throw new NotFoundException("未找到相关书籍");
        }
        bookService.deleteById(book.getId());
        return ResultUtil.generateSuccessResult("删除图书成功");
    }


}
