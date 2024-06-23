import { Menu } from 'antd'
import { Header } from 'antd/lib/layout/layout'

export default function AppHeader() {
    return (
        <Header style={{ display: 'flex', alignItems: 'center' }}>
            <div
                className='demo-logo'
                style={{
                    color: 'white',
                    fontSize: '1.5em',
                    fontWeight: 'bold',
                    marginRight: 16,
                }}
            >
                Logo
            </div>
            <Menu
                theme='dark'
                mode='horizontal'
                defaultSelectedKeys={['2']}
                // items={items}
                style={{ flex: 1, minWidth: 0 }}
            />
        </Header>
    )
}
