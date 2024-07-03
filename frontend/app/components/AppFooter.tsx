import { Footer } from 'antd/lib/layout/layout'

export default function AppFooter() {
    return (
        <Footer style={{ textAlign: 'center' }}>
            Applied Computing Concepts ©{new Date().getFullYear()} Created by
            AlgorithmAvengers Team
        </Footer>
    )
}
