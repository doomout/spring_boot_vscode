
//async 는 해당 함수가 비동기 처리를 위한 함수라는 것을 명시
async function get1(bno) {
    const result = await axios.get(`/replies/list/${bno}`) //await 은 비동기 호출하는 부분을 명시

    console.log(result)

    //return result.data
}


/*
  bno : 현재 게시물 번호
  page : 페이지 번호 
  size : 페이지당 사이즈
  goList : 마지막 페이지 호출 여부
*/
async function getList({bno, page, size, goList}) {

    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    return result.data
}