import { Menu } from 'antd'
import { Header } from 'antd/lib/layout/layout'
import Navbar from '@/app/components/Navbar'
import Logo from '@/app/components/Logo'

export default function AppHeader() {
    return (
        <Header
            style={{
                display: 'flex',
                alignItems: 'center',
                background: 'black',
            }}
        >
            <Logo />
            <Navbar />
        </Header>
    )
}
