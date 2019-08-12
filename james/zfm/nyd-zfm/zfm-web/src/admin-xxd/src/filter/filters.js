const ToFixedNum = value => {
    if(value){
        return Number(value).toFixed(2);
    }else{
        return '0.00'
    }
    
  }
const ToAddSymbol = value => {
    if(value && value == 'NaN'){
            return 'NaN'
    }else if(value && value == '/'){
            return '/'
    }else{
          return (Number(value)*100).toFixed(2) + '%';
    }
}

  export { ToFixedNum,ToAddSymbol }