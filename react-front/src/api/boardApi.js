
//게시판 관련 API

import api from "./axios";

export const boardApi ={

    //게시글 목록 조회 

    getList : async (params ={}) =>{
        const {page=1,size=10,condition,keyword} = params;
        const response = await api.get('/board/list2',{
            params : {page,size,condition,keyword}
        });

        return response.data;
    },

    //게시글 상세 조회
    getDetail : async (boardNo) =>{
        const response = await api.get(`/board/detail/${boardNo}`);


        return response.data;
    },

    //TOP 5 조회
    getTopList : async ()=>{
        const response = await api.get("/board/topList");

        return response.data;
    },

    //게시글 삭제
    delete : async (boardNo) =>{

        console.log('게시글 번호 : ',boardNo);

        const response = await api.delete(`/board/delete/${boardNo}`);

        // console.log(response);

        return response.data;
    },


    //게시글 검색 
    search : async (params) =>{
        //게시글 검색을 위한 검색 키워드,카테고리,페이지정보를 전달하기 
        const response = await api.get('/board/search',{params});
        
        // console.log(response)

        return response.data;
    },

    //게시글 작성
    create : async (formData) =>{
        const response = await api.post('/board/insert',formData,{
            headers : {
                'Content-Type' : 'multipart/form-data',
            },
        });

        // console.log(response);

        return response.data;
    },

    //게시글 수정
    update : async (formData) =>{
        const response = await api.put('/board/update',formData,{
            headers : {
                'Content-Type' : 'multipart/form-data',
            },
        });

        console.log(response);

        return response.data;
    }






}

export default boardApi;