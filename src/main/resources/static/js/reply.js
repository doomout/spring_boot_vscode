
//async 는 해당 함수가 비동기 처리를 위한 함수라는 것을 명시
async function get1(bno) {
    const result = await axios.get(`/replies/list/${bno}`) //await 은 비동기 호출하는 부분을 명시

    //console.log(result)

    return result.data
}


/*
  bno : 현재 게시물 번호
  page : 페이지 번호 
  size : 페이지당 사이즈
  goList : 마지막 페이지 호출 여부
*/
async function getList({bno, page, size, goLast}) {

    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    //마지막 페이지의 댓글을 보고 싶다면....
    if(goLast) {
      const total = result.data.total //댓글 전체수
      const lastPage = parseInt(Math.ceil(total/size)) //댓글 전체수/페이지를 계산한 뒤

      return getList({bno:bno, page:lastPage, size:size}) //게시판번호, 마지막 페이지 번호, 페이지 번호
    }

    return result.data
}

//댓글 등록
async function addReply(replyObj) {
  const response = await axios.post(`/replies/`, replyObj)
  return response.data
}