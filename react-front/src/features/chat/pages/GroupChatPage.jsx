import { useState } from "react";
import useWebsocket from "../hooks/useWebsocket";
import styles from './ChatPage.module.css';
import Button from "../../../components/commons/ui/Button";

//그룹채팅도 가능하도록 구현해보기 (연결 및 확인 해보세요)
function GroupChatPage(){
    
    //const url = 'ws://localhost:8080/spring/group';
    const [inputMessage,setInputMessage] = useState('');
    
    const {isConnected,message,connect,disconnect,sendMessage} = useWebsocket('/group');


    const handleSend = () =>{
        //공백만 있을경우 처리 
        if(!inputMessage.trim()) return;

        sendMessage(inputMessage); //메시지 전달함수에 입력값 넣기 
        setInputMessage(''); //전송했으니 빈칸 만들기 
    };

    const handleKeyDown = (e) =>{
        if(e.key === 'Enter'){
            handleSend(); //엔터키 누르면 메시지 전송하기 
        }

    }

    return (
        <div className='container'>
            <div className={styles.wrapper}>
                <h2 className={styles.title}>그룹 채팅 페이지</h2>


                <div className={styles.controls}>
                    <Button
                     variant="primary"
                     onClick={connect}
                     disabled={isConnected}
                    >
                    접속
                    </Button>
                    <Button
                        variant='danger'
                        onClick={disconnect}
                        disabled={!isConnected}
                    >
                        접속종료
                    </Button>
                    <span className={`${styles.stauts}${isConnected?styles.connected: ''}`}>
                        {isConnected? '🟢 연결됨' : '⭕ 연결안됨'}
                    </span>
                </div>

                <div className={styles.inputArea}>
                    <input type="text" 
                           value={inputMessage}
                           onChange={(e)=>setInputMessage(e.target.value)}
                           placeholder="메시지를 입력하세요"
                           className={styles.input}
                           disabled={!isConnected}
                           onKeyDown={handleKeyDown}
                    />
                    <Button variant="secondary" onClick={handleSend} disabled={!isConnected}>
                        전송
                    </Button>
                </div>

                <div className={styles.chatArea}>
                    {message.map((msg,index)=>(
                        <div key={index} className={styles.message}>
                            {msg}
                        </div>    
                    ))
                    }
                </div>
            </div>
        </div>
    );
}
export default GroupChatPage;