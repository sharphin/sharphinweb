window.onload = function() {
  const checkGet = document.getElementsByName("check");
  checkGet.forEach((check) => {
    check.addEventListener('click', () => {
      if(check.checked) {
        checkGet.forEach((allChecks) => {
          allChecks.checked = false;
        });
        check.checked = true;
      }
    });
  });
}