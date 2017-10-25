/******************************************************
	static.js
*******************************************************/

//var validateErrorMsgDisplayStyle = 'text';
var validateErrorMsgDisplayStyle = 'singleText';

/**
 * 是否一次性将所有错误都显示出来
 * true :显示所有
 * false:只显示一个错误
 */
var validateIsDisplayAllError = true;

var validateSpanNameOfErrorMsg = 'validateSpanNameOfErrorMsg';   //提示信息所属span元素的name属性值

/**
 * 一些css class变量
 */
var classNameOfValidateErrorMsg = "validate_error_message";       //错误提示信息的样式
var classNameOfValidateErrorInput = "validate_error_input";       //输入有误时,加给输入框的样式
var classNameOfValidateRequiredStar = "validate_required_star";   //必须输入项后面紧跟着一个星号的样式


var validatorMaxCount = 20;

//----------------- static method start -------------------------//
/**
 * @param name标签的name属性值
 * @param tagName标签的名字
 * 根据tagName和name属性获取对象
 * 获取不到返回null,否则返回数组
 * (该方法主要用于td,span等标签不能直接通过getElementsByName()获取其对象的)
 */
function getByNameAndTagName(name, tagName)
{
    var s = document.getElementsByTagName(tagName);
    if (!s)
    {
        return null;
    }
    var j = 0;
    var ret = new Array();
    for (var i = 0; i < s.length; i++)
    {
        if (s[i].name == name)
        {
            ret[j++] = s[i];
        }
    }
    return j == 0 ? null : ret;
}

var RegexValidator = function (regex){
	this.initialize(regex);
};
RegexValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {

	initialize:function(regex)
	{
		this.regexInitialize(regex);
	},
	
	regexInitialize:function(regex)
	{
		this.baseInitialize();
		this.regex = regex;
		this.message = validateI18nMsgRegex;
	},
	
	set:function(regex, message)
	{
		this.regex = regex;
		this.messageParams[0] = message;
		return this;
	},
	
	validate:function(str)
	{
		return this.regex.test(str);
	}
});



var RequiredValidator = function (){
	this.initialize();
};
RequiredValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {
	initialize: function()
	{
		this.requiredInitialize();
	},
	
	requiredInitialize: function()
	{
		this.baseInitialize();
		this.message = validateI18nMsgRequired;
		this.spanNameOfStar = "validatespanNameOfStar";
		this.starElement = "<span>*</span>";
	},
	
	validate:function(str)
	{
		if (str != null)
		{
			return !(/^\s*$/.test(str));
		}
	},

	/**
	 * 在字段后面加上星号
	 */
	doAfterAdd:function()
	{
		//this.clearStar();//先清空星号
		this.addStar();  //再加星号
		return;
	},
	
	/**
	 * 当注销后做些事情,子类视情况实现该函数,如Required验证器,需要去掉后面的红星号
	 * //子类根据具体情况覆盖此方法
	 */
	doAfterRemove:function()
	{
		//this.clearStar();    //先清空星号
		//this.addStar();      //再加星号
		this.clearMessage(); //清空错误消息
		return;
	},
	
	/**
	 * 添加星号
	 */
	addStar:function()
	{
		for (var i = 0; i < this.fieldArray.length; i++)
		{
			var fieldObj = document.getElementsByName(this.fieldArray[i].name);
			if (fieldObj)
			{
				for (var j = 0; j < fieldObj.length; j++)
				{
					var curObject = $("#"+(fieldObj[j].id));
					if (curObject.next().attr("name")==this.spanNameOfStar)continue;
					curObject.after(this.starElement);
					curObject.next().attr("name",this.spanNameOfStar);
					curObject.next().addClass(classNameOfValidateRequiredStar);
				}
			}
		}
	},
	
	/**
	 * 清空星号
	 */
	clearStar:function()
	{
		var spans = getByNameAndTagName(this.spanNameOfStar, "span");
		if (spans == null)
		{
			return;
		}
		for (var i = 0; i < spans.length; i++)
		{
			$(spans[i]).removeClass(classNameOfValidateRequiredStar);
			$(spans[i]).html("");
		}
	}
});

var EmailValidator = function (){
	this.initialize();
};
EmailValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {

    initialize:function()
    {
        this.emailInitialize();
    },
    
    emailInitialize:function()
    {
        this.baseInitialize();
        this.message = validateI18nMsgEmail;
    },
    
    /**
     * 说明:邮箱的验证代码是直接摘自validation-framework.js,非本人所写
     */
    validate:function(emailStr)
    {
       if (emailStr == null || emailStr == "")
       {
          return true;//不验证为空的串
       }
       var emailPat=/^(.+)@(.+)$/;
       var specialChars="\\(\\)<>@,;:\\\\\\\"\\.\\[\\]";
       var validChars="\[^\\s" + specialChars + "\]";
       var quotedUser="(\"[^\"]*\")";
       var ipDomainPat=/^(\d{1,3})[.](\d{1,3})[.](\d{1,3})[.](\d{1,3})$/;
       var atom=validChars + '+';
       var word="(" + atom + "|" + quotedUser + ")";
       var userPat=new RegExp("^" + word + "(\\." + word + ")*$");
       var domainPat=new RegExp("^" + atom + "(\\." + atom + ")*$");
       var matchArray=emailStr.match(emailPat);
       if (matchArray == null) {
           return false;
       }
       var user=matchArray[1];
       var domain=matchArray[2];
       if (user.match(userPat) == null) {
           return false;
       }
       var IPArray = domain.match(ipDomainPat);
       if (IPArray != null) {
           for (var i = 1; i <= 4; i++) {
              if (IPArray[i] > 255) {
                 return false;
              }
           }
           return true;
       }
       var domainArray=domain.match(domainPat);
       if (domainArray == null) {
           return false;
       }
       var atomPat=new RegExp(atom,"g");
       var domArr=domain.match(atomPat);
       var len=domArr.length;
       if ((domArr[domArr.length-1].length < 2) ||
           (domArr[domArr.length-1].length > 3)) {
           return false;
       }
       if (len < 2) {
           return false;
       }
       return true;
    }
});


var DatetimeValidator = function (){
	this.initialize();
};
DatetimeValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {

    initialize:function()
    {
        this.datetimeInitialize();
    },
    
    datetimeInitialize:function()
    {
        this.baseInitialize();
        this.message = validateI18nMsgDatetime;
    },
    
    set:function(pattern)
    {
    	this.pattern = pattern;
    	this.messageParams[0] = pattern;
    	return this;
    },
    
    /**
     * 说明:日期的验证代码是直接摘自validation-framework.js,略作修改,非本人所写
     */
    validate:function(value)
    {
        if (value == null || value == "")
        {
           return true;//不验证为空的串
        }
        
        var datePattern = this.pattern;//params[0];
		var dateLength = datePattern.length;	
		if ((dateLength != 10)&&(dateLength != 16)&&(dateLength != 19))
		{
			return false;
		}
		var patrn=/^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}|\d{4}-\d{2}-\d{2} \d{2}:\d{2}|\d{4}-\d{2}-\d{2}|\d{4}\/\d{2}\/\d{2} \d{2}:\d{2}:\d{2}|\d{4}\/\d{2}\/\d{2} \d{2}:\d{2}|\d{4}\/\d{2}\/\d{2})$/
		if (!patrn.exec(value))
		{
			return false;
		}
		return true;        
        
    }
});

var CharValidator = function (){
	this.initialize();
};
CharValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {

	initialize:function()
	{
		this.baseInitialize();
		this.message = validateI18nMsgChar;
	},
	
	validate:function(str)
	{
		return this.validateChar(str);
	},
	
	validateChar:function(str)
	{
		if (str == null || str == "")
		{
			return true;//不验证为空的串
		}
		try 
		{
			return (/^[A-Za-z]+$/.test(str))
		}
		catch (e)
		{
			return false; 
		}
		return true;
	}
});

var RawCharValidator = function (){
	this.initialize();
};
RawCharValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {

	initialize:function()
	{
		this.baseInitialize();
		this.message = validateI18nMsgRawChar;
	},
	
	validate:function(str)
	{
		return this.validateChar(str);
	},
	
	validateChar:function(str)
	{
		if (str == null || str == "")
		{
			return true;//不验证为空的串
		}
		try 
		{
			return (/^[0-9A-Za-z_.]+$/.test(str))
		}
		catch (e)
		{
			return false; 
		}
		return true;
	}
});

var CharNumValidator = function(){
	this.initialize();
};
CharNumValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {

	initialize:function()
	{
		this.baseInitialize();
		this.message = validateI18nMsgCharNum;
	},
	
	validate:function(str)
	{
		return this.validateCharNum(str);
	},
	
	validateCharNum:function(str)
	{
		if (str == null || str == "")
		{
			return true;//不验证为空的串
		}
		try 
		{
			return (/^[0-9A-Za-z]+$/.test(str));
		}
		catch (e)
		{
			return false; 
		}
		return true;
	}
});


var NotChineseValidator = function(){
	this.initialize();
};
NotChineseValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {

	initialize:function()
	{
		this.baseInitialize();
		this.message = validateI18nMsgNotChinese;
	},
	
	validate:function(str)
	{
		return this.validateNotChinese(str);
	},
	
	validateNotChinese:function(str)
	{
		if (str == null || str == "")
		{
			return true;//不验证为空的串
		}
		try 
		{
			if(/.*[\u4e00-\u9fa5]+.*$/.test(str)) { 
				return false; 
			} 
			return true; 
		}
		catch (e)
		{
			return false; 
		}
		return true;
	}
});

var CharNumUnderlineValidator = function(){
	this.initialize();
};
CharNumUnderlineValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {

	initialize:function()
	{
		this.baseInitialize();
		this.message = validateI18nMsgCharNumUnderline;
	},
	
	validate:function(str)
	{
		return this.validateCharNumUnderline(str);
	},
	
	validateCharNumUnderline:function(str)
	{
		if (str == null || str == "")
		{
			return true;//不验证为空的串
		}
		try 
		{
			 var reg = /^[a-zA-Z0-9_]+$/;
			 if (reg.test(str)){
				 return true;
			 }
			 return false;
		}
		catch (e)
		{
			return false; 
		}
		return true;
	}
});


var NumValidator = function(){
	this.initialize();
};
NumValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {

	initialize:function()
	{
		this.baseInitialize();
		this.message = validateI18nMsgNum;
	},
	
	validate:function(str)
	{
		return this.validateNum(str);
	},
	
	validateNum:function(str)
	{
		if (str == null || str == "")
		{
			return true;//不验证为空的串
		}
		try 
		{
			var test = (eval('str >= 0 || str <= 0'));
			return test;
		}
		catch (e)
		{
			return false;
		}
		return true;
	}
});

var IntValidator = function(){
	this.initialize();
};
IntValidator.prototype =  jQuery.extend(true,{},new NumValidator(), {

	initialize:function()
	{
		this.baseInitialize();
		this.message = validateI18nMsgInt;
	},
	
	validate:function(str)
	{
		return this.validateInt(str);
	},
	
	validateInt:function(str)
	{
		if (str == null || str == "")
		{
			return true;//不验证为空的串
		}
		try 
		{
			if (this.validateNum(str))
			{
				var test = (str % 10) + " ";
				if (test.indexOf(".") == -1)
				{
					return true;
				}
				else 
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch (e)
		{
			return false;
		}
	}
});


var NumRangeValidator = function(){
	this.initialize();
};
NumRangeValidator.prototype =  jQuery.extend(true,{},new NumValidator(), {
	initialize:function()
	{
		this.baseInitialize();
		this.message = validateI18nMsgNumRange;
	},
	
	/**
	 * 设置最小值,"--"表示无穷小
	 */
	setMin:function(min)
	{
		this.min = min;
		if (min == '--')//无穷小
		{
			if (this.max == "++")
			{
				this.message = validateI18nMsgNumRangeNum;
			}
			else
			{
				this.message = validateI18nMsgNumRangeMin;
				this.messageParams[0] = this.max;
			}
			
		}else
		{
			this.messageParams[0] = min;
		}
	},
	
	/**
	 * 设置最大值,"++"表示无穷大
	 */
	setMax:function(max)
	{
		this.max = max;
		if (max == '++')
		{
			if (this.min == "--")
			{
				this.message = validateI18nMsgNumRangeNum;
			}
			else
			{
				this.message = validateI18nMsgNumRangeMax;
				this.messageParams[0] = this.min;
			}
		}
		else
		{
			this.messageParams[0] = max;
		}
	},
	
	set:function(min, max)
	{
		this.setMin(min);
		this.setMax(max);
		return this;
	},
	
	validate:function(str)
	{
		if (str == null || str == "")
		{
			return true;//不验证为空的串
		}
		if (!this.validateNum(str))//必须为数字,否则验证不通过
		{
			return false;
		}
		try 
		{
			if (this.max == "++" && this.min == "--")
			{
				return true;
			}
						
			if (this.max == "++")
			{
				if (eval(str) < eval(this.min))
				{
					return false;
				}else 
				{
					return true;
				}
			}
			
			if (this.min == "--")
			{
				if (eval(str) > eval(this.max))
				{
					return false;
				}else 
				{
					return true;
				}
			}
			
			if (eval(str) > eval(this.max) || eval(str) < eval(this.min))
			{
				return false;
			}
			return true;
		}
		catch (e)
		{
			return false;
		}
	}
});


var CompareValidator = function (){
	this.initialize();
};
CompareValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {
	initialize:function()
	{
		this.compareInitialize();
	},
	compareInitialize:function()
	{
		this.baseInitialize();
		
	},
	
	/**
	 * @param compareType 比较类型 'n':数字比较; 'v':字符串比较
	 * @param operator 比较符,可以为'<','<=','==','!=','>','>='
	 * @param field 被比较的字段
	 * 举例:
	 * var field1 = new Field("用户名", "loginName");
	 * compareValidator.set('n','>',field1);//要求添加此验证器的字段必须大于field1的值
	 */
	set:function(compareType, operator, field)
	{
		this.compareType = compareType;
		this.operator = operator;
		this.comparedField = field;
		
		this.messageParams[0] = validateOperatorLabelMap[this.operator];
		this.messageParams[1] = field.label;
		
		if (compareType == 'n')
		{
			this.message = validateI18nNumCompare;
		}
		else if (compareType == 'v')
		{
			this.message = validateI18nStringCompare;
		}
		else 
		{
			alert("验证框架使用错误:CompareValidator的比较类型不支持'" + compareType + "'");
		}
		return this;
	},
	
	validateByFilter:function(filter)
    {
        var ret = true;
        for (var i = 0; i < this.fieldArray.length; i++)
        {
            var elements = document.getElementsByName(this.fieldArray[i].name);
            if (elements)
            {
                for (var j = 0; j < elements.length; j++)
                {
                    /**不需要验证或者验证通过则继续下一个元素的处理*/
                    if ( (!filter.filter(elements[j])) || (this.validate(elements[j].value, j)) )
                    {
                        continue;
                    }
                    else
                    {
                        this.processInvalid(elements[j], this.fieldArray[i]);
                        ret = false;
                        if (!validateIsDisplayAllError)
                        {
                            return false;
                        }
                    }
                }
            }
        }
        return ret;
    },
	
	validate:function(str, index)
	{
		if (str == null || str == "")
		{
			return true;
		}
		var elements = document.getElementsByName(this.comparedField.name);
		var comparedValue = (elements[index]) ? elements[index].value : elements[0].value;//"";//elements[index].value;

		if (comparedValue == null || comparedValue == "")
		{
			return true;
		}
		this.messageParams[2] = comparedValue;
		var s = null;
		if (this.compareType == "n")//是数字比较
		{
			var numV = new NumValidator();
			if ( (!numV.validate(str)) || (!numV.validate(comparedValue)) )//不是数字则验证不通过
			{
				return false;
			}
			
			s = str + this.operator + comparedValue;
		}
		else if (this.compareType == "v")//是字符串比较
		{
			s = "\"" + str + "\"" + this.operator + "\"" + comparedValue + "\"";
		}
		return eval(s) == true;
	}
});


var LengthLimitValidator = function(){
	this.initialize();
};
LengthLimitValidator.prototype =  jQuery.extend(true,{},new BaseValidator(), {
	initialize:function()
	{
		this.baseInitialize();
		this.message = validateI18nMsgLengthRange;
	},
	set:function(limit)
	{
		this.limit = limit;
		this.messageParams[0] = limit;
		return this;
	},
	validate:function(value)
	{
		try 
		{
			if(value.charLength() > this.limit)   
			{   
				return false;
			}
		}
		catch (e)
		{
			return false;
		}
		return true;
	}
});

String.prototype.charLength=function()
{   
	var reg = /[\u4e00-\u9fa5]/;   
	var len = this.length;   
	var p = len;   
	for(var i=0;i<len;i++)   
	{   
		if(reg.test(this.charAt(i)))p++;
	}   
	return p;
};

var ValidatorFactory =  function (){
	this.initialize();
};
ValidatorFactory.prototype=
{
	initialize: function()
	{
		this.validatorArray = new Array();
	},
	
	/**
	 * 将所有的验证器注销
	 */
	removeAll: function()
	{
		for (var i = 0; i < this.validatorArray.length; i++)
		{
			this.validatorArray[i].removeAll();
		}
	},
	
	/**
	 * 
	 * 使用是validatorFactory.add(validator1, validator2,validator3... ... validatorx);
	 */
	add:function()
	{
		var startIndex = this.validatorArray.length;
		for (var i = 0; i < arguments.length; i++)
		{
			this.validatorArray[i + startIndex] = arguments[i];
		}
	},
	
	validate:function()
	{
		return this.validateByFilter(new DefaultFilter(null));
	},
	
	validateByFilter:function(filter)
	{
		var ret = true;
		var baseValidator = new BaseValidator();
		baseValidator.clearMessage();
		for (var i = 0; i < this.validatorArray.length; i++)
		{
			if (!this.validatorArray[i].validateByFilter(filter))
			{
				ret = false;
				if (!validateIsDisplayAllError)
				{
					return false;
				}
			}
		}
		return ret;
	},
	
	validateForm:function(formNames)
	{
		return this.validateByFilter(new FormFilter(formNames));
	}
};


/*******************************************************
	validator.config and validator.fascad
********************************************************/
var validateI18nMsgRequired = "{0}不能为空!";
var validateI18nMsgEmail = "{0}格式不正确!";
var validateI18nMsgInt = "{0}必须为整数!";

var validateI18nMsgDatetime = "{0}格式不正确,应为{1}";

var validateI18nMsgLengthRange = "{0}必须不能大于{1}个字符!";

var validateI18nMsgNumRange = "{0}必须为数字,且在{1}和{2}之间!";
var validateI18nMsgNumRangeNum = "{0}必须为数字!";
var validateI18nMsgNumRangeMin = "{0}必须为数字,且小于等于{1}!";
var validateI18nMsgNumRangeMax = "{0}必须为数字,且大于等于{1}!";

var validateI18nMsgChar = "{0}必须为字母!";
var validateI18nMsgRawChar = "{0}必须为字母、数字、下划线或者点符号!";
var validateI18nMsgCharNum = "{0}只能包含字母和数字!";
var validateI18nMsgNum = "{0}必须为数字!";
var validateI18nMsgRegex = "{0}不合法!{1}";
var validateI18nStringCompare = "{0}必须{1}{2}[{3}]!";
var validateI18nNumCompare = "{0}必须为数字,且{1}{2}[{3}]!";
var validateI18nMsgNotChinese = "{0}不能包含汉字!";
var validateI18nMsgCharNumUnderline = "{0}只能包含字符、数字或者下划线!";

var validateOperatorLabelMap = new Array();
validateOperatorLabelMap["<"] = "小于";
validateOperatorLabelMap["<="] = "小于或等于";
validateOperatorLabelMap["=="] = "等于";
validateOperatorLabelMap["!="] = "不等于";
validateOperatorLabelMap[">"] = "大于";
validateOperatorLabelMap[">="] = "大于或等于";

function validate()
{
	if(!validatorFactory.validate())
    {
		return false;
    }
    return true;
}
function validateForm()
{
    var formNames = arguments;
    if(!validatorFactory.validateForm(formNames))
    {
        return false;
    }
    return ret;
}
function cancelValidate()
{
	validatorFactory.removeAll();
}

var validatorFactory = new ValidatorFactory();
var requiredValidator = new RequiredValidator();
var numRangeValidator = new NumRangeValidator();
var charValidator = new CharValidator();
var notChineseValidator = new NotChineseValidator();
var rawCharValidator = new RawCharValidator();
var charNumValidator = new CharNumValidator();
var charNumUnderlineValidator = new CharNumUnderlineValidator();
var numValidator = new NumValidator();
var intValidator = new IntValidator();
var regexValidator = new RegexValidator();
var emailValidator = new EmailValidator();
var datetimeValidator = new DatetimeValidator();
var compareValidator = new CompareValidator();
var lengthValidator = new LengthLimitValidator();

var numRangeValidators = new Array();
var compareValidators = new Array();
var regexValidators = new Array();
var datetimeValidators = new Array();
var lengthValidators = new Array();

for (var i = 0; i < validatorMaxCount; i++)
{
    numRangeValidators[i] = new NumRangeValidator();
    compareValidators[i] = new CompareValidator();
    regexValidators[i] = new RegexValidator();
    datetimeValidators[i] = new DatetimeValidator();
    lengthValidators[i] = new LengthLimitValidator();
    validatorFactory.add(numRangeValidators[i], 
    					  compareValidators[i],
    					  datetimeValidators[i],
    					  regexValidators[i],
    					  lengthValidators[i]);
}
validatorFactory.add(requiredValidator, 
					  numRangeValidator, 
					  charValidator,
					  notChineseValidator,
					  charNumUnderlineValidator,
					  rawCharValidator,
					  charNumValidator,
					  numValidator,
					  regexValidator, 
					  emailValidator,
					  datetimeValidator,
					  compareValidator, 
					  intValidator,
					  lengthValidator);//intValidator要放到compareValidator的后面