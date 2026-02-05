
import styles from './ChatSelectPage.module.css';
import {Link} from 'react-router-dom';

function ChatSelectPage() {

    const chatOptions = [
        //각 요청 페이지 상태값 
        {
            path : '/chat/basic',
            title : '기본 채팅',
            description : '간단한 WebSocket 채팅 구현',
        },
         {
            path : '/chat/group',
            title : '그룹 채팅',
            description : '그룹 WebSocket 채팅 구현',
        },

         {
            path : '/chat/member',
            title : '멤버 채팅',
            description : '회원 전용 WebSocket 채팅 구현',
        },

         {
            path : '/chat/private',
            title : '개인 채팅',
            description : '1:1 귓속말 WebSocket 채팅 구현',
        },
    ];


    return (
        <div className="container">
            <div className={styles.wrapper}>
                <h2 className={styles.title}>채팅 서비스</h2>
                <p className={styles.subtitle}>원하는 채팅 서비스를 선택하세요</p>

                <div className={styles.grid}>
                    {chatOptions.map((option)=>(
                        // 각 옵션별로 이동할 수 있도록 Link 설정하기

                        <Link key={option.path} to={option.path} className={styles.card}>
                            <h3 className={styles.cardTitle}>{option.title}</h3>
                            <p className={styles.cardDesc}>{option.description}</p>
                            <span className={styles.arrow}>➡️</span>
                        </Link>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default ChatSelectPage;