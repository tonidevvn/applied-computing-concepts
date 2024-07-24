import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import './globals.css'
import { AntdRegistry } from '@ant-design/nextjs-registry'
import { ThemeProvider } from './components/theme-provider'
import AppHeader from '@/app/components/AppHeader'
import { Content } from 'antd/lib/layout/layout'
import { Layout } from 'antd'
import AppFooter from '@/app/components/AppFooter'
import { AppStoreProvider } from '@/stores/app-store-provider'
import bg from '../public/background.jpg'
const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
    title: 'Food Price Analysis',
    description: 'Demo app for advanced computing concepts',
}

export default function RootLayout({
    children,
}: Readonly<{
    children: React.ReactNode
}>) {
    return (
        <html lang='en'>
            <body className={inter.className}>
                <AppStoreProvider>
                    <ThemeProvider>
                        <main>
                            <AntdRegistry>
                                <Layout
                                    style={{
                                        backgroundImage: `url(${bg.src})`,
                                        backgroundSize: '100% auto',
                                        backgroundRepeat: 'repeat-y'
                                    }}
                                >
                                    <AppHeader />
                                    <Content style={{ padding: '0 48px' }}>
                                        {children}
                                    </Content>
                                    <AppFooter />
                                </Layout>
                            </AntdRegistry>
                        </main>
                    </ThemeProvider>
                </AppStoreProvider>
            </body>
        </html>
    )
}
