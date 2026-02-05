import { useCallback, useEffect, useRef, useState } from "react";


//websocket 채팅 hook 
export function useWebsocket(mapping){
    const [isConnected,setIsConnected] = useState(false);
    const [message, setMessage] = useState([]);
    const [userList,setUserList] = useState([]);
    const socketRef = useRef(null);
    //공통 url 처리 
    const url = `ws://localhost:8080/spring${mapping}`;


    //연결작업
    const connect = useCallback(()=>{


        //이미 연결 상태일때 재연결 요청 방지 
        if(socketRef?.current?.readyState == WebSocket.OPEN){
            console.log("이미 연결되어 있습니다.");
            return;
        }

        //웹소캣 객체 생성 
        const socket = new WebSocket(url);
        socketRef.current = socket; //ref로 dom요소 직접접근시키기(웹소켓객체)


        socket.onopen = ()=>{
            console.log('WebSocket 연결 성공');
            setIsConnected(true);
        };

        //메시지 왔을때 처리 
        socket.onmessage = (e) => {
            //이벤트객체에서 추출한 데이터로 메시지 처리하기
            const data = JSON.parse(e.data); //JSON데이터 받았으니 파싱작업하기 
            //유저 목록인 경우 유저 목록 갱신 

            if(data.userList){//유저 목록
                setUserList(data.userList); //목록 갱신
            }else{//일반 메시지 

                 //기존 메시지에 새로운 메시지 추가 
                setMessage((prev)=>[...prev,data]);

                console.log(data);

            }

          
        };

        socket.onclose = () =>{
            console.log('WebSocket 연결 종료');
            setIsConnected(false);
        };


        socket.onerror = (error) =>{
            console.log('WebSocket 에러',error);
        };
    },[url]);//url 변경시 함수 재생성

    //종료 처리 
    const disconnect = useCallback(()=>{
        if(socketRef.current){//웹소켓 객체가 있을때 
            socketRef.current.close();
            socketRef.current = null; 
            setMessage([]);
            setUserList([]);
        }
    },[]);


    //메시지 전송 
    const sendMessage = useCallback((message)=>{

        //소켓이 열려있으면 메시지 전송 
        if(socketRef.current?.readyState===WebSocket.OPEN){

            if(typeof message ==='object'){//객체형태라면 
                //json 문자열로 변경하여 전달 
                socketRef.current.send(JSON.stringify(message));
            }else{//일반 메시지 형태(문자열)
                socketRef.current.send(message);
            }

        }else{
            console.error('웹소켓에 연결되어있지 않습니다.');
        }
        


    },[]);

    //컴포넌트 언마운트시 연결 종료 useEffect로 처리 
    useEffect(()=>{

        return () =>{
            disconnect(); //연결 종료 함수 호출
        }

    },[disconnect]); //disconnect 함수 의존처리 


    return {
        isConnected,
        message,
        userList,
        connect,
        disconnect,
        sendMessage,
        setMessage,
    };

}

export default useWebsocket;