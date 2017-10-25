var DefaultFilter = function (params){
	this.initialize(params);
};
DefaultFilter.prototype = 
{
	initialize:function(params)
	{
		this.defaultInitialize(params);
	},
	
	defaultInitialize:function(params)
	{
		this.params = params;
	},
	
	/**
	 * @return true:表示需要验证,false:表示被过滤了,不需要验证.
	 */
	filter:function(element)
	{
		return true;//默认都需要验证
	}
};

/************************************************
	FormFilter.js
*************************************************/
var FormFilter = function (params){
	this.initialize(params);
};
FormFilter.prototype =  jQuery.extend(true,{},new DefaultFilter(), 
{
	initialize:function(params)
	{
		this.defaultInitialize(params);
	},
	
	filter:function(element)
	{
		try 
		{
			for (var i = 0; i < this.params.length; i++)
			{
				if (element.form.name == this.params[i])
				{
					return true;
				}
			}
			return false;
		}
		catch (e) 
		{
			return false;
		}
	}
});