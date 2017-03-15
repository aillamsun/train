package com.train.core.web.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.train.core.utils.MessageUtils;
import com.train.core.web.response.HttpCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 通用Controller
 * Created by sungang on 2016/9/26.
 */
public abstract class BaseCRUDController<T extends Serializable> extends AbstarctBaseController {

//    private Logger log = Logger.getLogger(BaseCRUDController.class);

    protected SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    protected final String UPLOAD_IMAGE_PATH = "/upload"+ File.separator+"img/";
    protected final String UPLOAD_DOC_PATH = "/upload" + File.separator + "doc/";

    protected final String ADD = getViewPrefix() + "/add";
    protected final String EDIT = getViewPrefix() + "/edit";
    protected final String DETAIL = getViewPrefix() + "/detail";
    protected final String LIST = getViewPrefix() + "/list";


//    @Resource
//    private HttpServletResponse response;
//
//    @Resource
//    private HttpServletRequest request;

    /**
     * 验证Bean实例对象
     */
//    @Autowired
//    protected Validator validator;


    /**
     * 线程安全初始化reque，respose对象
     *
     * @param request
     * @param response
     * @ModelAttribute 表示该Controller的所有方法在调用前，先执行此@ModelAttribute方法
     */
    @ModelAttribute
    public void init(HttpServletRequest request, HttpServletResponse response) {
        //此处可以验证每个方法的访问权限


        currentRequest.set(request);
        currentResponse.set(response);
    }

//    @Override
//    protected BaseService getBaseService() {
//        return null;
//    }


//    @InitBinder
//    protected void initBinder(WebDataBinder binder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        dateFormat.setLenient(false);
////        binder.registerCustomEditor(Date.class,new CustomDateEditor(dateFormat, false));
//    }


    /**
     * 功能说明：通用列表分页查询方法，通用Pager的处理页面分页
     * 解决Jquery Table分页问题
     *
     * @param model
     * @param item
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public String listForPage(Model model, @ModelAttribute T item) {
        try {
            HttpServletRequest request = getRequest();
            //处理 Jquery Table分页问题
            String iDisplayStart = request.getParameter("iDisplayStart");
            String iDisplayLength = request.getParameter("iDisplayLength");
            Integer pageNum = iDisplayStart != null ? Integer.valueOf(iDisplayStart) : 1; //从第几条数据开始检索
            Integer pageSize = iDisplayLength != null ? Integer.valueOf(iDisplayLength) : 15; //从第几条数据开始检索


            pageNum = pageNum / pageSize + 1;
            String sortIndex = request.getParameter("iSortCol_0");//排序索引
            String search = request.getParameter("sSearch");//搜索值
            String sortField = request.getParameter("mDataProp_" + sortIndex);
            String sortType = request.getParameter("sSortDir_0");//排序方式
            String sEcho = request.getParameter("sEcho"); // 点击的次数
//            if (StringUtils.isEmpty(sortField)) {
//                sortField = "id";
//            }
            if (StringUtils.isEmpty(sortType)) {
                sortType = "desc";
            }

            PageHelper.startPage(pageNum, pageSize, true);
            if (!StringUtils.isEmpty(sortField)) {
//                PageHelper.orderBy(sortField + " " + sortType);
            }
            //分页处理
            List<T> results = getBaseService().select(item);
            PageInfo pageInfo = new PageInfo<T>(results);
            Map<String, Object> dataMap = Maps.newHashMap();
            dataMap.put("data", results);
            model.addAttribute("data", dataMap);
            model.addAttribute("flag", SUCESS);
            model.addAttribute("recordsTotal", results.size());
            model.addAttribute("sEcho", sEcho);
            model.addAttribute("sSearch", search);
            model.addAttribute("recordsFiltered", pageInfo.getTotal());
        } catch (Exception ex) {
            model.addAttribute("flag", ERROR);
            ex.printStackTrace();
        }
        return LIST;
    }


    /**
     * 功能说明：Ajax 通用列表分页查询方法，通用Pager的处理页面分页
     * 解决Jquery Table分页问题
     *
     * @param item
     * @return
     */
    @RequestMapping(value = "ajax/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listAjaxForPage(@ModelAttribute T item) {
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> dataMap = Maps.newHashMap();
        try {
            HttpServletRequest request = getRequest();
            //处理 Jquery Table分页问题
            String iDisplayStart = request.getParameter("iDisplayStart");
            String iDisplayLength = request.getParameter("iDisplayLength");
            Integer pageNum = iDisplayStart != null ? Integer.valueOf(iDisplayStart) : 1; //从第几条数据开始检索
            Integer pageSize = iDisplayLength != null ? Integer.valueOf(iDisplayLength) : 15; //从第几条数据开始检索
            if (pageNum == 0) {
                pageNum = 1;
            } else {
                pageNum = pageNum / pageSize + 1;
            }
            String sortIndex = request.getParameter("iSortCol_0");//排序索引
            String search = request.getParameter("sSearch");//搜索值
            String sortField = request.getParameter("mDataProp_" + sortIndex);
            String sortType = request.getParameter("sSortDir_0");//排序方式
            String sEcho = request.getParameter("sEcho"); // 点击的次数
//            if (StringUtils.isEmpty(sortField)) {
//                sortField = "id";
//            }
            if (StringUtils.isEmpty(sortType)) {
                sortType = "desc";
            }
            PageHelper.startPage(pageNum, pageSize, true);
            if (!StringUtils.isEmpty(sortField)) {
//                PageHelper.orderBy(sortField + " " + sortType);
            }
            //分页处理
            List<T> results = getBaseService().select(item);
            PageInfo pageInfo = new PageInfo<T>(results);
            resultMap.put("recordsTotal", results.size());
            resultMap.put("sEcho", sEcho);
            resultMap.put("sSearch", search);
            resultMap.put("recordsFiltered", pageInfo.getTotal());
            resultMap.put("aaData", results);
            dataMap.put("data", resultMap);
            dataMap.put("flag", SUCESS);
        } catch (Exception ex) {
            resultMap.put("flag", ERROR);
            ex.printStackTrace();
        }
        return dataMap;
    }

    /**
     * 功能说明：通用获取列表查询
     *
     * @return
     */
    @RequestMapping("listAll")
    @ResponseBody
    public Map<String, Object> listAll(@ModelAttribute T item) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            List<T> results = getBaseService().select(item);
            resultMap.put("flag", SUCESS);
            resultMap.put("data", results);
        } catch (Exception ex) {
            resultMap.put("flag", ERROR);
            ex.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 功能说明：通用跳转到新增页面
     *
     * @return
     * @updated
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add() {
        return ADD;
    }

    /**
     * 功能说明：通用跳转到编辑页面
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit/{id}")
    public String edit(Model model, @PathVariable("id") String id) {
        try {
            getData(model, id);
        } catch (Exception ex) {
            model.addAttribute("flag", ERROR);
        }
        return EDIT;
    }

    /**
     * 功能说明：异步通用获取详情数据
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "asyn/detail/{id}")
    @ResponseBody
    public Map<String, Object> viewAsyn(Model model, @PathVariable("id") String id) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            T result = null;
            if (StringUtils.isNumeric(id)){
                boolean flag = false;
                try{
                    result = (T) getBaseService().selectByKey(Integer.parseInt(id));
                }catch (Exception e){
                    flag = true;
                }
                if(flag){
                    result = (T) getBaseService().selectByKey(Long.parseLong(id));
                }
            }else {
                result = (T) getBaseService().selectByKey(id);
            }
            resultMap.put("data", result);
            resultMap.put("code", HttpCode.SUCCESS);
        } catch (Exception ex) {
            resultMap.put("code", HttpCode.ERROR);
            ex.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 功能说明：通用获取详情数据
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "detail/{id}")
    public String view(Model model, @PathVariable("id") String id) {
        try {
            getData(model, id);
        } catch (Exception ex) {
            model.addAttribute("flag", ERROR);
            ex.printStackTrace();
        }
        return DETAIL;
    }


    private void getData(Model model, String id) {
        Integer tempId = null;
        if (StringUtils.isNotEmpty(id)) {
            tempId = Integer.valueOf(id);
        }
        T result = (T) getBaseService().selectByKey(tempId);
        model.addAttribute("data", result);
        model.addAttribute("flag", SUCESS);
    }

    /**
     * 功能说明：通用保存操作
     *
     * @param item
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(@RequestBody T item) {
        Map<String, Object> resultMap = Maps.newHashMap();
        int c = 0;
        try {
            c = getBaseService().insertSelective(item);
            resultMap.put("code", HttpCode.SUCCESS);
            if (c == 0) {
                resultMap.put("code", HttpCode.ERROR);
            }
        } catch (Exception ex) {
            resultMap.put("code", HttpCode.ERROR);
            resultMap.put("data", MessageUtils.message("sys.exception"));
            ex.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 功能说明：通用更新操作
     *
     * @param item
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> update(@ModelAttribute T item) {
        Map<String, Object> resultMap = Maps.newHashMap();
        int c = 0;
        try {
            c = getBaseService().updateByPrimaryKeySelective(item);
            resultMap.put("code", HttpCode.SUCCESS);
            if (c == 0) {
                resultMap.put("code", HttpCode.ERROR);
            }
        } catch (Exception ex) {
            resultMap.put("code", HttpCode.ERROR);
            resultMap.put("data", MessageUtils.message("sys.exception"));
            ex.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 能说明：通用删除操作 （可用过批量或者单个）
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delele", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteOrBatch(String ids) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            int c = 0;
            if (ids.contains(",")) {
                c = getBaseService().deleteByIds(ids);
            } else {
                if (StringUtils.isNumeric(ids)) {
                    c = getBaseService().deleteByPrimaryKey(Long.valueOf(ids));
                } else {
                    c = getBaseService().deleteByPrimaryKey(ids);
                }
            }
            if (c == 0) {
                resultMap.put("code", HttpCode.ERROR);
            } else {
                resultMap.put("code", HttpCode.SUCCESS);
            }
        } catch (Exception ex) {
            resultMap.put("code", HttpCode.ERROR);
            resultMap.put("data", MessageUtils.message("sys.exception"));
            ex.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 功能说明：通用删除操作
     *
     * @param id
     */
    @RequestMapping(value = "/delele/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(@PathVariable String id) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            int c = 0;
            if (StringUtils.isNumeric(id)) {
                c = getBaseService().deleteByPrimaryKey(Long.valueOf(id));
            } else {
                c = getBaseService().deleteByPrimaryKey(id);
            }
            if (c == 0) {
                resultMap.put("code", HttpCode.ERROR);
            } else {
                resultMap.put("code", HttpCode.SUCCESS);
            }
        } catch (Exception ex) {
            resultMap.put("code", HttpCode.ERROR);
            resultMap.put("data", MessageUtils.message("sys.exception"));
            ex.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 线程安全
     */
    public HttpServletResponse getResponse() {
        return (HttpServletResponse) currentResponse.get();
    }

    /**
     * 线程安全
     */
    public HttpServletRequest getRequest() {
        return (HttpServletRequest) currentRequest.get();
    }


//    /**
//     * 获取Resuest 请求参数
//     * @param request
//     * @return
//     */
//    protected Map<String, String> getParameterMap(HttpServletRequest request) {
//        Map<String, String> params = Maps.newHashMap();
//        Map requestParams = request.getParameterMap();
//        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
//            String name = (String) iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
//            }
//            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
//            params.put(name, valueStr);
//        }
//        return params;
//    }


    /**
     * 得到ModelAndView
     *
     * @return
     */
    public ModelAndView getModelAndView() {
        return new ModelAndView();
    }


    /**
     * 获取保存在Session中的用户对象
     //     * @param request
     * @return
     */
//    protected User getSessionUser(HttpServletRequest request) {
//        return (User) request.getSession().getAttribute(Constants.CURRENT_USER);
//    }


    /**
     * 添加Model消息
     *
     * @param messages
     */
    protected void addMessage(Model model, String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
        }
        model.addAttribute("message", sb.toString());
    }

    /**
     * 客户端返回JSON字符串
     *
     * @param response
     * @param object
     * @return
     */
    protected String renderString(HttpServletResponse response, Object object) {
        return renderString(response, JSON.toJSONString(object), "application/json");
    }

    /**
     * 客户端返回字符串
     *
     * @param response
     * @param string
     * @return
     */
    protected String renderString(HttpServletResponse response, String string, String type) {
        try {
            response.reset();
            response.setContentType(type);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
            return null;
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * 参数绑定异常
     */
//    @ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
//    public String bindException() {
//        return "error/400";
//    }

    /**
     * 授权登录异常
     */
//    @ExceptionHandler({AuthenticationException.class})
//    public String authenticationException() {
//        return "error/403";
//    }


    /**
     * 初始化数据绑定
     * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
     * 2. 将字段中Date类型转换为String类型
     */
//    @InitBinder
//    protected void initBinder(WebDataBinder binder) {
//        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
//        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
//            @Override
//            public void setAsText(String text) {
//                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
//            }
//            @Override
//            public String getAsText() {
//                Object value = getValue();
//                return value != null ? value.toString() : "";
//            }
//        });
//        // Date 类型转换
//        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
//            @Override
//            public void setAsText(String text) {
////                setValue(DateUtils.parseDate(text));
//            }
////			@Override
////			public String getAsText() {
////				Object value = getValue();
////				return value != null ? DateUtils.formatDateTime((Date)value) : "";
////			}
//        });
//    }
}
