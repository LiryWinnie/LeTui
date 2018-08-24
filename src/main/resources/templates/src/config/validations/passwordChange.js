import validator from 'validator';
import isEmpty from 'lodash/isEmpty';

const validateInput=(data)=>{
    let errors={};
    if(validator.isEmpty(data.oldpassword)){
        errors.oldpassword="原密码不能为空！";
    }
    if(validator.isEmpty(data.newpassword)){
        errors.newpassword="新密码不能为空！"
    }
    if(validator.isEmpty(data.newpasswordconfirmation)){
        errors.newpasswordconfirmation="确认密码不能为空！"
    }
    if(!validator.equals(data.newpassword,data.newpasswordconfirmation)){
        errors.newpasswordconfirmation="两次密码必须相同！";
    }
    return{
        errors,
        isValid:isEmpty(errors)
    }
};

export default validateInput;