import React, { ReactNode } from 'react'
import { ConfigProvider } from 'antd'

interface Props {
    children?: ReactNode
}

export function ThemeProvider({ children }: Props) {
    return (
        <ConfigProvider
            theme={{
                token: {
                    // Seed Token
                    // colorPrimary: '#00b96b',
                    // borderRadius: 2,
                    // Alias Token
                    // colorBgContainer: '#f6ffed',
                },
                cssVar: true,
                hashed: false,
            }}
        >
            {children}
        </ConfigProvider>
    )
}
