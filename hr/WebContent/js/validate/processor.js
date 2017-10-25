var Processor = function (){
	this.initialize();
};
Processor.prototype =
{
	initialize:function()
	{
		
	},
	
	process:function()
	{
		
	}
};



var InvalidProcessor = function (element, field, validator){
	this.initialize(element, field, validator);
};
InvalidProcessor.prototype = jQuery.extend(true,{},new Processor(), 
{
	initialize:function(element, field, validator)
	{
		this.invalidInitialize(element, field, validator);
	},
	
	invalidInitialize:function(element, field, validator)
	{
		this.element = element;
		this.field = field;
		this.validator = validator;
	},
	
	process:function()
	{
		this.defaultProcess();
		/*this.element.addClassName(classNameOfValidateErrorInput);*/
	},
	
	defaultProcess:function()
	{
		if (this.element.style.display == 'none')
        {
            return;
        }
        try
	    {
	        this.element.select(true);
	    } 
	    catch (e) 
	    {
	    }
	    try
	    {
	        this.element.focus(true);
	    } 
	    catch (e) 
	    {
	    }		
	}
});





var AlertInvalidProcessor = function (element, field, validator){
	this.initialize(element, field, validator);
};
AlertInvalidProcessor.prototype = jQuery.extend(true,{},new InvalidProcessor(), 
{
	initialize:function(element, field, validator)
	{
		this.invalidInitialize(element, field, validator);
	},
	
	process:function()
	{
		this.defaultProcess();
		this.element.addClassName(classNameOfValidateErrorInput);
        alert(this.validator.getMessage(this.field.label));
	}
});





var TextInvalidProcessor = function (element, field, validator){
	this.initialize(element, field, validator);
};
TextInvalidProcessor.prototype = jQuery.extend(true,{},new InvalidProcessor(), 
{
	initialize:function(element, field, validator)
	{
		this.invalidInitialize(element, field, validator);
	},
	
	process:function()
	{
		this.defaultProcess();
		var s = "<span>" + this.validator.getMessage(this.field.label) + "</span>";
        new Insertion.After(this.element, s);
        this.element.next(0).name = validateSpanNameOfErrorMsg;
        this.element.next(0).addClassName(classNameOfValidateErrorMsg);
        this.element.addClassName(classNameOfValidateErrorInput);
        
        try 
        {
        	//this.validator.comparedField.name
        }catch (e)
        {
        	//
        }
	}
});


var SingleTextInvalidProcessor =function SingleTextInvalidProcessor(element, field, validator){
	this.initialize(element, field, validator);
};
SingleTextInvalidProcessor.prototype = jQuery.extend(true,{},new InvalidProcessor(), 
{
	initialize:function(element, field, validator)
	{
		this.invalidInitialize(element, field, validator);
	},
	
	process:function()
	{
		this.defaultProcess();
		var label =  $(this.element).attr('label');
		var errMsg = this.validator.getMessage(label);
		writeErrorMsg(errMsg);
        //new Insertion.After(this.element, s);
        //this.element.next(0).name = validateSpanNameOfErrorMsg;
        //this.element.next(0).addClassName(classNameOfValidateErrorMsg);        
        
        try 
        {
        	this.element.addClass(classNameOfValidateErrorInput);
        }catch (e)
        {
        	$(this.element).addClass(classNameOfValidateErrorInput);
        }
	}
});



var InvalidProcessorFactory = function (){
	this.initialize();
};
InvalidProcessorFactory.prototype =
{
	initialize:function()
	{
		
	},
	create:function(element, field, validator)
	{
		if (validateErrorMsgDisplayStyle == 'text')
		{
			return new TextInvalidProcessor(element, field, validator);
		}
		else if (validateErrorMsgDisplayStyle == 'alert')
		{
			return new AlertInvalidProcessor(element, field, validator);
		}
		else if (validateErrorMsgDisplayStyle == 'singleText')
		{
			return new SingleTextInvalidProcessor(element, field, validator);
		}		
		else 
		{
			alert('validateErrorMsgDisplayStyle');
			return new TextInvalidProcessor(element, field, validator); 
		}
	}
};